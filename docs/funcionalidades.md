# Funcionalidades del sistema

Sistema de inscripciones universitarias construido con microservicios Spring Boot y un frontend React. Este documento lista las funcionalidades disponibles, organizadas por módulo y guiadas por los controladores REST de cada servicio.

Todas las rutas pasan por el **api-gateway** (puerto 8080). Las rutas `/api/**` requieren JWT y se autorizan por permiso (`RECURSO:OPERACIÓN`); las rutas `/auth/**` las atiende el authorization-server; `/webhooks/**` es público (validado por firma HMAC).

## Índice

- [Autenticación y cuenta](#autenticación-y-cuenta)
- [RBAC: roles, permisos y vistas](#rbac-roles-permisos-y-vistas)
- [Catálogo académico](#catálogo-académico)
- [Estudiantes](#estudiantes)
- [Inscripciones](#inscripciones)
- [Pagos (Mercado Pago)](#pagos-mercado-pago)
- [Dashboard de estadísticas](#dashboard-de-estadísticas)
- [Notificaciones por correo](#notificaciones-por-correo)
- [Auditoría](#auditoría)
- [Pantallas del frontend](#pantallas-del-frontend)

---

## Autenticación y cuenta

Servicio: **authorization-server** — `AuthController` (`/auth`)

| Método | Ruta | Funcionalidad |
|---|---|---|
| POST | `/auth/login` | Inicio de sesión con usuario/contraseña. Devuelve JWT; si la cuenta tiene MFA activo, responde `twoFactorRequired` y hay que completar la verificación TOTP. |
| POST | `/auth/register` | Registro de un nuevo usuario. |
| GET | `/auth/validateToken` | Validación centralizada del JWT. La usa el `AuthFilter` del gateway en cada request a `/api/**`; los servicios downstream confían en el gateway. |
| POST | `/auth/2fa/init` | Inicia la activación de MFA: genera el secreto TOTP y la URL `otpauth://` (código QR). |
| POST | `/auth/2fa/confirm` | Confirma la activación de MFA validando el primer código TOTP. |
| POST | `/auth/2fa/verify` | Verifica el código TOTP durante el login y emite el JWT definitivo. |
| GET | `/auth/profile` | Perfil del usuario autenticado. |
| GET | `/auth/user` | Lista de usuarios (para asignación de roles). |
| GET | `/auth/user/{userId}` | Datos resumidos de un usuario. |

Además del login con credenciales, el servicio soporta **OAuth2 con GitHub y Google** (Spring Security); tras autenticarse con el proveedor, el usuario es redirigido al frontend (`oauth2/redirect`) con su token.

## RBAC: roles, permisos y vistas

Servicio: **authorization-server**. El modelo de permisos es `RECURSO:OPERACIÓN:ALCANCE` (p. ej. `ENROLLMENT:READ:ALL`). Las *vistas de UI* (`UI_VIEW`) controlan qué módulos ve cada rol en el frontend y también protegen el catálogo académico en el backend.

| Método | Ruta | Funcionalidad |
|---|---|---|
| GET/POST | `/auth/rbac/permissions` | Listar / crear permisos. |
| GET/PUT/DELETE | `/auth/rbac/permissions/{id}` | Consultar / actualizar / eliminar un permiso. |
| GET/POST | `/auth/rbac/roles` | Listar / crear roles (con sus permisos y vistas). |
| GET/PUT/DELETE | `/auth/rbac/roles/{id}` | Consultar / actualizar / eliminar un rol. |
| GET/POST | `/auth/rbac/views` | Listar / crear vistas de UI. |
| GET/PUT/DELETE | `/auth/rbac/views/{code}` | Consultar / actualizar / eliminar una vista por código. |
| PUT | `/auth/rbac/users/{userId}/roles` | Asignar roles a un usuario. |

En el enrollment-server, el `PermissionInterceptor` valida el header `X-User-Permissions` (inyectado por el gateway) contra el recurso de la ruta: `/api/v1/student` → `STUDENT`, `/api/v1/enrollment` → `ENROLLMENT`, y el resto (catálogo, dashboard) → `UI_VIEW`. La operación se deriva del método HTTP (GET→READ, POST→CREATE, PUT/PATCH→UPDATE, DELETE→DELETE).

## Catálogo académico

Servicio: **enrollment-server** (`/api/v1`). Protegido con el recurso `UI_VIEW`.

### Facultades — `FacultyController`

| Método | Ruta | Funcionalidad |
|---|---|---|
| GET | `/api/v1/faculty` | Listar facultades. |
| GET | `/api/v1/faculty/{id}` | Consultar una facultad. |
| POST | `/api/v1/faculty` | Crear facultad. |
| PUT | `/api/v1/faculty/{id}` | Actualizar facultad. |

### Carreras — `CareerController`

| Método | Ruta | Funcionalidad |
|---|---|---|
| GET | `/api/v1/career` | Listar carreras (asociadas a una facultad). |
| POST | `/api/v1/career` | Crear carrera. |
| PUT | `/api/v1/career/{id}` | Actualizar carrera. |

### Cursos — `CourseController`

| Método | Ruta | Funcionalidad |
|---|---|---|
| GET | `/api/v1/course` | Listar cursos (código, nombre, créditos, carrera). |
| POST | `/api/v1/course` | Crear curso. |
| PUT | `/api/v1/course/{id}` | Actualizar curso. |

### Vigencias / periodos académicos — `TermController`

| Método | Ruta | Funcionalidad |
|---|---|---|
| GET | `/api/v1/term` | Listar periodos (código tipo `2025-2`, fechas de inicio/fin, activo). No se permiten periodos superpuestos. |
| POST | `/api/v1/term` | Crear periodo. |
| PUT | `/api/v1/term/{id}` | Actualizar periodo. |

### Cursos en vigencia — `CourseOfferingController`

Un *course offering* es la oferta de un curso en un periodo: sección, capacidad, cupos ocupados y precio de inscripción.

| Método | Ruta | Funcionalidad |
|---|---|---|
| GET | `/api/v1/course-offering` | Listar cursos en vigencia. |
| POST | `/api/v1/course-offering` | Publicar un curso en un periodo (sección, capacidad, precio). |
| PUT | `/api/v1/course-offering/{id}` | Actualizar la oferta. |

## Estudiantes

Servicio: **enrollment-server** — `StudentController`. Protegido con el recurso `STUDENT`.

| Método | Ruta | Funcionalidad |
|---|---|---|
| GET | `/api/v1/student` | Listar estudiantes. |
| GET | `/api/v1/student/{id}` | Consultar un estudiante. |
| POST | `/api/v1/student` | Registrar estudiante (documento, nombres, email, teléfono, nacimiento, dirección). |
| PUT | `/api/v1/student/{id}` | Actualizar datos / activar-desactivar. |

## Inscripciones

Servicio: **enrollment-server** — `EnrollmentController`. Protegido con el recurso `ENROLLMENT`. Es el flujo central del sistema.

| Método | Ruta | Funcionalidad |
|---|---|---|
| GET | `/api/v1/enrollment` | Listado **paginado** (`page`, `size`, por defecto 10) ordenado de más reciente a más antiguo, con filtros opcionales `studentId`, `termId`, `courseId`. Devuelve `{content, page, size, totalElements, totalPages}`. |
| GET | `/api/v1/enrollment/{enrollmentId}` | Detalle de una inscripción. |
| POST | `/api/v1/enrollment` | Inscribir un estudiante en un curso en vigencia. Valida duplicados y cupos, incrementa el contador de inscritos, crea la preferencia de pago en Mercado Pago y emite el evento de dominio (outbox) que dispara el correo con el enlace de pago. |
| PUT | `/api/v1/enrollment/{enrollmentId}` | Cambiar el estado (`PENDING`, `PAID`, `CANCELLED`, `COMPLETED`). Cancelar libera el cupo. |
| DELETE | `/api/v1/enrollment` | Dar de baja (unenroll) a un estudiante de un curso completado; libera el cupo y emite evento. |

Ciclo de vida de una inscripción:

```
PENDING ──(pago aprobado / webhook)──> PAID ──> COMPLETED
   │                                     │
   └────────────── CANCELLED <───────────┘   (cancelada libera cupo; estado terminal)
```

## Pagos (Mercado Pago)

Servicio: **enrollment-server** — `MercadoPagoWebhookController` (`/webhooks`, **fuera** de `/api`: no requiere JWT ni permisos, pensado para que Mercado Pago lo invoque).

| Método | Ruta | Funcionalidad |
|---|---|---|
| POST | `/webhooks/mercadopago` | Recibe notificaciones de pago. Valida la firma HMAC `x-signature` (secreto `MP_WEBHOOK_SECRET`), consulta el pago en la API de MP y, si está `approved`, transiciona la inscripción `PENDING → PAID` (idempotente), guarda `payment_id`/`payment_status`/`paid_at` y emite el evento que dispara el correo de confirmación. |

Detalles del flujo (Checkout Pro):

- Al crear la inscripción se genera una **preferencia de pago** con `external_reference` = id de inscripción y el precio del curso en vigencia (fallback `MP_ENROLLMENT_FEE`).
- El enlace de pago (`init_point`) viaja en el correo de inscripción ("Pagar inscripción"). La creación de la preferencia es *fail-soft*: si MP no responde, la inscripción igual se crea y el correo sale sin enlace.
- Las `back_urls` regresan al frontend público en `/payments/:status` (success / failure / pending).
- Pagos inexistentes o inscripciones no encontradas responden 200 (para que MP no reintente); errores transitorios responden 500 (MP reintenta).

## Dashboard de estadísticas

Servicio: **enrollment-server** — `DashboardController`. Protegido con `UI_VIEW:READ`.

| Método | Ruta | Funcionalidad |
|---|---|---|
| GET | `/api/v1/dashboard/stats` | Estadísticas agregadas: total de estudiantes y activos, total de cursos, cursos en vigencia activos, total de inscripciones, inscripciones por estado, **periodo vigente** (término activo cuya fecha contiene hoy) con sus inscripciones, inscripciones por periodo y top 5 de cursos con más inscripciones. |

El frontend las muestra en el módulo **Estadísticas** (`/app/statistics`, accesible desde el dashboard) con tarjetas KPI y gráficos (Recharts): dona de estados, barras por periodo y ranking de cursos.

## Notificaciones por correo

Servicio: **notification-server** (sin API REST; consume Kafka).

- Consume el topic `enrollment.notifications` (eventos publicados por el enrollment-server vía patrón Outbox).
- Envía correos (SMTP, Mailtrap en desarrollo) según el evento:
  - **Inscripción creada**: correo de confirmación con botón "Pagar inscripción" si hay enlace de Mercado Pago.
  - **Inscripción pagada**: correo de confirmación de pago.
  - Cambios de estado relevantes (cancelación / baja).

## Auditoría

Servicio: **event-store-server** (sin API REST; consume Kafka).

- El `AuditGlobalFilter` del gateway publica **cada request** al sistema como `AuditEvent` en el topic `audit.requests` (usuario, ruta, método, respuesta).
- El event-store-server consume el topic y persiste el rastro de auditoría en su propia base de datos (`postgres-events`).

## Pantallas del frontend

SPA en React 19 + TypeScript (`frontend/`), rutas protegidas según sesión, permisos y vistas de UI del rol:

| Ruta | Pantalla |
|---|---|
| `/login`, `/register` | Inicio de sesión (credenciales u OAuth2 GitHub/Google) y registro. |
| `/2fa-verify` | Verificación TOTP durante el login. |
| `/payments/:status` | Resultado del pago de Mercado Pago (pública). |
| `/app` | Dashboard: accesos rápidos a los módulos visibles según permisos. |
| `/app/statistics` | Estadísticas del sistema: KPIs y gráficos del flujo de inscripciones. |
| `/app/faculty`, `/app/career`, `/app/course` | CRUD del catálogo académico. |
| `/app/term`, `/app/course-offering` | Gestión de periodos y cursos en vigencia. |
| `/app/students` | Gestión de estudiantes, con detalle e historial de inscripciones. |
| `/app/enrollment` | Inscripciones: listado paginado con filtros por estudiante/periodo/curso, creación, detalle y cambio de estado. |
| `/app/profile` | Perfil del usuario y activación de MFA (QR TOTP). |
| `/app/rbac/roles`, `/app/rbac/permissions`, `/app/rbac/users` | Administración de roles, permisos y asignación a usuarios. |

## Documentación interactiva (Swagger)

El gateway agrega la documentación OpenAPI de los servicios: con el stack levantado, `http://localhost:8080/swagger-ui.html` (docs individuales en `/v3/api-docs/auth-service` y `/v3/api-docs/enrollment-service`).

---

Ver también: [arquitectura.md](./arquitectura.md) para la vista técnica del sistema (servicios, seguridad, eventos, observabilidad).
