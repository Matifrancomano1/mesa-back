package com.example.demo.service.software;

import com.example.demo.domain.hardware.Equipo;
import com.example.demo.domain.software.InstalacionSoftware;
import com.example.demo.domain.software.Licencia;
import com.example.demo.domain.software.ProductoSoftware;
import com.example.demo.domain.software.TipoLicencia;
import com.example.demo.dto.software.*;
import com.example.demo.mapper.software.SoftwareMapper;
import com.example.demo.repository.hardware.EquipoRepository;
import com.example.demo.repository.software.InstalacionSoftwareRepository;
import com.example.demo.repository.software.LicenciaRepository;
import com.example.demo.repository.software.ProductoSoftwareRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SoftwareService {

    private final ProductoSoftwareRepository productoRepository;
    private final LicenciaRepository licenciaRepository;
    private final InstalacionSoftwareRepository instalacionRepository;
    private final EquipoRepository equipoRepository;
    private final SoftwareMapper mapper;

    // --- PRODUCTOS ---
    @Transactional(readOnly = true)
    public Page<ProductoSoftwareDto> getProductos(Boolean activo, Pageable pageable) {
        if (activo != null) {
            return productoRepository.findByActivo(activo, pageable).map(mapper::toDto);
        }
        return productoRepository.findAll(pageable).map(mapper::toDto);
    }

    @Transactional
    public ProductoSoftwareDto createProducto(ProductoSoftwareRequest request) {
        if (productoRepository.findByNombreAndVersion(request.getNombre(), request.getVersion()).isPresent()) {
            throw new IllegalStateException("El producto con esa versión ya existe");
        }
        ProductoSoftware entity = new ProductoSoftware();
        entity.setNombre(request.getNombre());
        entity.setVersion(request.getVersion());
        entity.setFabricante(request.getFabricante());
        entity.setDescripcion(request.getDescripcion());
        entity.setRequiereLicencia(request.isRequiereLicencia());
        entity.setActivo(true);
        return mapper.toDto(productoRepository.save(entity));
    }

    // --- LICENCIAS ---
    @Transactional(readOnly = true)
    public Page<LicenciaDto> getLicencias(Long productoId, Pageable pageable) {
        if (productoId != null) {
            return licenciaRepository.findByProductoId(productoId, pageable).map(mapper::toDto);
        }
        return licenciaRepository.findAll(pageable).map(mapper::toDto);
    }

    @Transactional
    public LicenciaDto createLicencia(LicenciaRequest request) {
        if (licenciaRepository.findByClave(request.getClave()).isPresent()) {
            throw new IllegalStateException("La clave de licencia ya existe");
        }
        ProductoSoftware producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
                
        Licencia entity = new Licencia();
        entity.setClave(request.getClave());
        entity.setTipo(TipoLicencia.valueOf(request.getTipo()));
        entity.setCantidadTotal(request.getCantidadTotal());
        entity.setFechaVencimiento(request.getFechaVencimiento());
        entity.setProducto(producto);
        entity.setActivo(true);
        // pre-persist sets cantidadEnUso to 0
        return mapper.toDto(licenciaRepository.save(entity));
    }

    // --- INSTALACIONES ---
    @Transactional(readOnly = true)
    public Page<InstalacionSoftwareDto> getInstalaciones(Long equipoId, Long licenciaId, Long productoId, Pageable pageable) {
        if (equipoId != null) {
            return instalacionRepository.findByEquipoId(equipoId, pageable).map(mapper::toDto);
        }
        if (licenciaId != null) {
            return instalacionRepository.findByLicenciaId(licenciaId, pageable).map(mapper::toDto);
        }
        if (productoId != null) {
            return instalacionRepository.findByProductoId(productoId, pageable).map(mapper::toDto);
        }
        return instalacionRepository.findAll(pageable).map(mapper::toDto);
    }

    @Transactional
    public InstalacionSoftwareDto createInstalacion(InstalacionSoftwareRequest request) {
        if (instalacionRepository.existsByEquipoIdAndProductoId(request.getEquipoId(), request.getProductoId())) {
            throw new IllegalStateException("El equipo ya tiene instalado este producto");
        }

        Equipo equipo = equipoRepository.findById(request.getEquipoId())
                .orElseThrow(() -> new EntityNotFoundException("Equipo no encontrado"));
                
        ProductoSoftware producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

        Licencia licencia = null;
        if (request.getLicenciaId() != null) {
            licencia = licenciaRepository.findById(request.getLicenciaId())
                    .orElseThrow(() -> new EntityNotFoundException("Licencia no encontrada"));
                    
            if (!licencia.getProducto().getId().equals(producto.getId())) {
                throw new IllegalStateException("La licencia no corresponde al producto a instalar");
            }
            
            if (licencia.getCantidadEnUso() >= licencia.getCantidadTotal()) {
                throw new IllegalStateException("No hay cupos disponibles para esta licencia");
            }
            
            licencia.setCantidadEnUso(licencia.getCantidadEnUso() + 1);
            licenciaRepository.save(licencia);
        } else if (producto.isRequiereLicencia()) {
            throw new IllegalStateException("El producto requiere una licencia para ser instalado");
        }

        InstalacionSoftware entity = new InstalacionSoftware();
        entity.setEquipo(equipo);
        entity.setProducto(producto);
        entity.setLicencia(licencia);
        entity.setFechaInstalacion(request.getFechaInstalacion() != null ? request.getFechaInstalacion() : java.time.LocalDate.now());
        entity.setObservaciones(request.getObservaciones());
        entity.setActivo(true);

        return mapper.toDto(instalacionRepository.save(entity));
    }
    
    @Transactional
    public void deleteInstalacion(Long id) {
        InstalacionSoftware entity = instalacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Instalación no encontrada"));
                
        if (entity.getLicencia() != null) {
            Licencia licencia = entity.getLicencia();
            licencia.setCantidadEnUso(Math.max(0, licencia.getCantidadEnUso() - 1));
            licenciaRepository.save(licencia);
        }
        
        instalacionRepository.delete(entity);
    }
}
