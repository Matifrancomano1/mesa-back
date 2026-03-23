# Guía del Sistema - Backend Mesa de Ayuda (Spring Boot)

Esta guía resume la arquitectura y los módulos del backend desarrollado en Java (Spring Boot) para el sistema de Mesa de Ayuda del Poder Judicial. 

El proyecto sigue una arquitectura en capas estándar: **Dominio (Entities) -> Repositorios -> Servicios -> Controladores**, comunicándose con el exterior a través de **DTOs** mapeados mediante **MapStruct**.

## 1. Infraestructura Core y Seguridad
- **`config/`**: Configuraciones generales del proyecto. Destacan `SecurityConfig` (define rutas públicas/privadas), `OpenApiConfig` (Swagger) y `AuditConfig` (Auditoría JPA).
- **`domain/common/BaseAuditableEntity.java`**: Clase base de la cual heredan casi todas las entidades. Provee campos automáticos de auditoría (`creadoEn`, `creadoPor`, `modificadoEn`, `modificadoPor`) y borrado lógico (`activo`).
- **`exception/GlobalExceptionHandler.java`**: Atrapa errores comunes (como `EntityNotFoundException` o errores de validación) y los formatea como respuestas HTTP limpias (`ErrorResponse`).
- **`security/`**: Maneja la autenticación sin estado:
  - `JwtUtil.java`: Generación y validación de tokens JWT.
  - `JwtAuthenticationFilter.java`: Filtro que intercepta las peticiones para extraer y validar el Bearer token, inyectando el usuario en el contexto de Spring.

## 2. Descripción de los Módulos de Negocio

### Seguridad (`/seguridad`)
Control de acceso basado en roles granulares (RBAC).
- **Archivos Clave**: `Usuario`, `Rol`, `Funcion`.
- **Flujo**: Define quién puede hacer qué mediante anotaciones `@PreAuthorize` en los controladores (ej. `hasAuthority('CASO_CREAR')`). `AuthService` gestiona el login y el refresco de tokens.

### Organización Territorial (`/organizacion`)
La jerarquía geográfica y física del sistema judicial.
- **Archivos Clave**: `Circunscripcion` -> `Distrito` -> `Ciudad` -> `Edificio` -> `Juzgado`.
- **Propósito**: Toda la estructura está anidada, donde el `Juzgado` es el nodo final ubicable en un `Edificio` específico.

### Hardware (`/hardware`)
Gestión de inventario físico.
- **Archivos Clave**: `ClaseHardware`, `TipoHardware`, `Marca`, `Modelo` (Catálogos) y `Equipo` (inventario real).
- **Propósito**: El `Equipo` utiliza Spring Data Envers (`@Audited`) para guardar automáticamente un historial de todas sus modificaciones a lo largo del tiempo.

### Software (`/software`)
Gestión del software y sus ciclos de vida.
- **Archivos Clave**: `ProductoSoftware` (el programa), `Licencia` (las llaves adquiridas) e `InstalacionSoftware` (vínculo entre equipo físico, producto de software y consumo de una licencia).

### Contratos y Proveedores (`/contrato`)
Seguimiento administrativo.
- **Archivos Clave**: `Proveedor` y `Contrato`.
- **Propósito**: Mantiene registro de los servicios y SLA de terceros. `ContratoService` calcula automáticamente alertas ("VIGENTE", "POR_VENCER", "URGENTE", "VENCIDO") basadas en la fecha de finalización.

### Puestos de Trabajo (`/puesto`)
Relación entre el personal y los recursos.
- **Archivos Clave**: `PuestoTrabajo`.
- **Propósito**: Vincula a una persona de un `Juzgado` específico con uno o más `Equipo`s físicos, consolidando a quién pertenece qué cosa.

### Mesa de Ayuda / Casos (`/caso`)
El módulo central de gestión de incidencias.
- **Archivos Clave**: `Caso` y `BitacoraEntrada`.
- **Flujo Operativo (Máquina de Estados)**:
  1. **SOLICITADO**: Se crea el caso con un autogenerado de número (Ej: CASO-2026-00123).
  2. **ASIGNADO**: Un técnico es asignado al caso (`/casos/{id}/asignar`).
  3. **EN_CURSO**: El técnico indica que empezó a trabajar (`/casos/{id}/iniciar`).
  4. **CERRADO**: Se aporta una nota técnica final de resolución (`/casos/{id}/cerrar`).
- **Bitácora**: `BitacoraEntrada` almacena un registro histórico de comentarios, tanto privados (solo técnicos) como públicos (visibles por el usuario), funcionando como un hilo de chat dentro del ticket.

### Dashboard (`/dashboard`)
Vistas gerenciales.
- **Archivos Clave**: `DashboardDto`, `DashboardService`.
- **Propósito**: Ejecuta consultas agregadas rápidas que cuentan casos abiertos, contratos por vencer, licencias expiradas, equipos dañados, proporcionando datos vitales para la vista de inicio.

## 3. Rutas API, Métodos y Controladores

A continuación se detallan los endpoints principales expuestos por el backend, sus métodos HTTP y sus correspondientes archivos controladores (`.java`). La mayoría implementa operaciones CRUD estándar (`GET`, `POST`, `PUT`, `DELETE`).

### Autenticación y Seguridad
- **AuthController** (`AuthController.java`)
  - `POST /auth/login` - Autenticación de usuario.
  - `POST /auth/refresh` - Refresco de token JWT.
  - `POST /auth/logout` - Cerrar sesión.
  - `GET /auth/me` - Obtener datos del usuario actual.
- **UsuarioController** (`UsuarioController.java`)
  - Base: `/v1/usuarios` (CRUD estándar: `GET`, `GET /{id}`, `POST`, `PUT /{id}`, `DELETE /{id}`)
- **RolController** (`RolController.java`)
  - Base: `/v1/roles` (CRUD estándar)

### Mesa de Ayuda (Casos)
- **CasoController** (`CasoController.java`)
  - Base: `/v1/casos` (Soporta: `GET` listado paginado/filtrado, `GET /{id}`, `POST` crear)
  - Transiciones de Estado:
    - `PATCH /v1/casos/{id}/asignar`
    - `PATCH /v1/casos/{id}/iniciar`
    - `PATCH /v1/casos/{id}/cerrar`
    - `PATCH /v1/casos/{id}/reabrir`
  - Bitácora:
    - `GET /v1/casos/{id}/bitacora`
    - `POST /v1/casos/{id}/bitacora`

### Organización Territorial
- **CircunscripcionController** (`CircunscripcionController.java`)
  - Base: `/v1/circunscripciones` (CRUD estándar)
- **DistritoController** (`DistritoController.java`)
  - Base: `/v1/distritos` (CRUD estándar)
- **CiudadController** (`CiudadController.java`)
  - Base: `/v1/ciudades` (CRUD estándar)
- **EdificioController** (`EdificioController.java`)
  - Base: `/v1/edificios` (CRUD estándar)
- **JuzgadoController** (`JuzgadoController.java`)
  - Base: `/v1/juzgados` (CRUD estándar)

### Hardware e Inventario
- **EquipoController** (`EquipoController.java`)
  - Base: `/v1/equipos` (CRUD estándar)
- **HardwareCatalogosController** (`HardwareCatalogosController.java`)
  - Base: `/v1/hardware` (Catálogos y clasificaciones)

### Software
- **SoftwareController** (`SoftwareController.java`)
  - Productos: `/v1/software` (`GET`, `POST`)
  - Licencias: `/v1/licencias` (`GET`, `POST`)
  - Instalaciones: `/v1/instalaciones` (`GET`, `POST`, `DELETE /{id}`)

### Contratos y Proveedores
- **ProveedorController** (`ProveedorController.java`)
  - Base: `/v1/proveedores` (CRUD estándar)
- **ContratoController** (`ContratoController.java`)
  - Base: `/v1/contratos` (CRUD estándar)

### Puestos de Trabajo
- **PuestoTrabajoController** (`PuestoTrabajoController.java`)
  - Base: `/v1/puestos` (CRUD estándar)

### Dashboard
- **DashboardController** (`DashboardController.java`)
  - Base: `/v1/dashboard` (`GET` métricas agregadas)

---
**Nota de Ejecución y Migraciones**: 
La estructura de base de datos se genera a través de migraciones de **Flyway** (carpeta `src/main/resources/db/migration`). El backend se ejecuta con `mvn spring-boot:run` y expone automáticamente un visor de APIs en `/swagger-ui.html`.
