# Refactor: matrícula por Carrera+Periodo (CareerOffering)

> Archivo de progreso de la sesión. Si se corta la sesión/tokens, retomar desde aquí.
> Plan completo: `~/.claude/plans/estabamos-en-el-proceso-noble-sky.md`

## Nuevo modelo de dominio

- **`CareerOffering`** = carrera + periodo (term). Aquí viven el **precio**, `capacity` y `enrolledCount`. Es a lo que se matricula y lo que paga el estudiante (MP usa `CareerOffering.price`).
- **`CareerCourse`** = malla curricular: `careerId` + `courseId` + `semesterLevel`. Un curso puede estar en varias carreras y el ciclo varía por carrera.
- **`Course`**: ya no tiene `careerId` ni `semesterLevel`. Las asignaciones van en `careers: [{careerId, semesterLevel}]` de Create/UpdateCourseCommand (mínimo 1).
- **`CourseOffering`**: solo catálogo/secciones (curso+term+sección+capacity). Sin `price` ni `enrolledCount`.
- **`Enrollment`**: referencia `careerOfferingId` (antes `courseOfferingId`). Filtro de listado por `careerId` (antes `courseId`).
- **`EnrollmentAssignedEvent`** (common): `careerName` + `termCode` (antes `courseName`). Emails dicen "matrícula"/"Pagar matrícula".
- API nueva: `POST/GET/PUT /api/v1/career-offering` (permiso vista `CAREER_OFFERING_LIST`, ya en `secure.sql`).

## Checklist

### Backend — HECHO (verificado 2026-07-11, pendiente solo compilar)
- [x] Dominio, JPA, mappers, adapters, repos de CareerOffering/CareerCourse
- [x] Enrollment → careerOfferingId (dominio, JPA, queries, controller, DTOs)
- [x] Course/CourseOffering depurados; CourseApplicationService asigna carreras
- [x] PaymentApplicationService/MercadoPagoPaymentAdapter usan precio de CareerOffering
- [x] Dashboard backend: `topCareers`, `activeCareerOfferings`
- [x] notification-server: emails con carrera+periodo
- [x] secure.sql: permiso CAREER_OFFERING_LIST en roles
- [x] Frontend feature `career-offering/` (page/form/dialog/hooks/service/types) + rutas/endpoints/keys/routeProtection

### Pendiente (esta sesión)
- [x] Paso 0: este archivo + memoria persistente
- [x] Paso 1: `mvn -pl common,enrollment-server,notification-server -am package -DskipTests` → **compila limpio, sin errores residuales**
- [x] Paso 2a: `useCareerOfferingColumns()` agregado en `frontend/src/config/columns.tsx`
- [x] Paso 2b: módulo "Carreras en Vigencia" agregado en `allModules` de `DashboardPage.tsx` (icono BookUp)
- [x] Paso 3: feature `course` migrada: types con `careers[]`, CourseForm con useFieldArray, CoursePage payloads, CourseDetailDialog muestra malla, columna "Carreras" con badges; además `CareerCourse` del frontend ganó `semesterLevel` y CareerDetailDialog muestra "Ciclo N" (CareerResponse.courseList ahora es CourseWithLevelResponse)
- [x] Paso 4: feature `course-offering`: `price`/`enrolledCount` eliminados de types/form/page/columnas; `CourseOfferingResponse.course` ahora tipado como summary (`CourseOfferingCourse`); etiqueta "Carrera"→"Curso" corregida en el form
- [x] Paso 5: feature `enrollment` migrada: request `careerOfferingId`, EnrollmentForm con selector de CareerOffering (muestra precio), EnrollmentSearchForm filtra por carrera, service/hook renombrados a `...AndCareerId`, response `careerOffering`, columnas Carrera+Periodo, EnrollmentDetailDialog muestra carrera/periodo/precio/capacidad; StudentDetailDialog (historial de matrículas) también migrado
- [x] Paso 6: feature `dashboard`: types y DashboardStats.tsx con `activeCareerOfferings`/`topCareers`, KPI "Carreras en vigencia", gráfico "Carreras con más matrículas"
- [x] Paso 7a: `npm run build` (tsc + vite) pasa limpio. `npm run lint`: 1 error preexistente en `components/ui/badge.tsx` (react-refresh/only-export-components, archivo shadcn no tocado) y 2 warnings preexistentes — ninguno introducido por el refactor.
- [ ] Paso 7b: **PENDIENTE (requiere acción del usuario)**: `make reset` para regenerar la BD con el nuevo esquema (bloqueado por permisos: destruye volúmenes). Luego flujo manual: curso con 2 carreras/ciclos → career-offering (carrera+periodo+precio) → matrícula → email "Pagar matrícula" con precio del CareerOffering → dashboard con topCareers.
- [x] Paso 8: CLAUDE.md actualizado (nueva sección "Enrollment domain model"; payments usa `CareerOffering.price`, botón "Pagar matrícula")

### Correcciones post-prueba (2026-07-11, tarde)
- [x] Bug: `EnrollmentApplicationService.getAllEnrollmentCourses` lanzaba `IllegalArgumentException` al listar sin filtro de carrera (`new CareerID(null)`; CareerID valida no-nulo a diferencia de StudentID/TermID). Ahora los tres filtros opcionales se envuelven solo si vienen informados. Requiere rebuild del contenedor (`make up`).
- [x] UX: CourseForm ahora tiene cabeceras "Carrera"/"Ciclo" sobre la lista de asignaciones y texto explicando que el ciclo es el semestre de la malla en esa carrera.
- [x] Decisión del usuario: **CourseOffering eliminado por completo**. Backend: borrados 19 archivos (dominio, DTOs, mapper, use cases, servicio, controller, JPA entity/repo/mapper/adapter, excepciones, `CourseSummaryResponse` y `CourseMapper.toSummaryResponse` que solo él usaba) + limpiado `GlobalExceptionHandler`. Frontend: borrada `features/course-offering/`, limpiados `columns.tsx`, `routes.tsx`, `path.ts`, `routeProtection.ts`, `endpoints.ts`, `keys.ts`, `DashboardPage.tsx`. `secure.sql`: quitado `COURSE_OFFERING_LIST` (CAREER_OFFERING_LIST pasa a orden 7). CLAUDE.md actualizado. Backend y frontend compilan. La tabla `course_offering` desaparecerá con el próximo `make reset`.

### Módulo Aulas + Secciones (2026-07-11, decisiones del usuario: sección = curso+periodo+aula, capacidad en el aula, sin horario v1)
- [x] Backend: `Classroom` (code, name?, capacity nullable, virtual, active — aula física exige capacity > 0, validado en `ClassroomApplicationService` con `InvalidClassroomException`) y `Section` (courseId+termId+classroomId+sectionCode, unique (course,term,code)). Hexagonal completo: dominio, repos, DTOs, mappers, use cases, servicios, controllers `/api/v1/classroom` y `/api/v1/section`, JPA (tablas `classroom`, `section`), GlobalExceptionHandler. Se re-agregó `CourseSummaryResponse` + `CourseMapper.toSummaryResponse` (los usa SectionResponse). Compila.
- [x] secure.sql: vistas `CLASSROOM_LIST` (orden 8) y `SECTION_LIST` (orden 9) + agregadas a ambos roles.
- [x] Frontend: features `classroom/` y `section/` completas (types/service/hooks/form/dialog/page), columnas `useClassroomColumns`/`useSectionColumns`, rutas `/app/classroom` y `/app/section`, routeProtection, endpoints, keys, módulos dashboard (iconos DoorOpen/Layers). Form de aula: checkbox "Aula virtual" deshabilita/limpia capacidad. Build OK.
- [ ] Probar tras `make reset`: crear aula física y virtual → crear sección (curso+periodo+aula) → verificar unique (curso, periodo, código de sección).

### Regla de negocio secciones (2026-07-11, análisis con el usuario)
- Decisión: **Section se queda como curso+periodo+aula (sin careerId)** — las clases son compartidas: alumnos de distintas carreras asisten a la misma sección si el curso está en su malla (la carrera se deriva de `CareerCourse`).
- [x] Hueco cerrado: ya no se puede crear una sección en un periodo sin carreras ofertadas. Backend: `SectionApplicationService.validateCourseOfferedInTerm` (curso debe tener ≥1 carrera de su malla con `CareerOffering` **activa** en el periodo, si no → `InvalidSectionException` 400); nuevo `CareerOfferingRepository.findAllByTermId`. Frontend: en `SectionForm` el select de periodo depende del curso elegido (deshabilitado hasta elegir curso; opciones = periodos de ofertas activas de carreras de la malla del curso; `termId` se resetea al cambiar de curso).

## Notas
- Los "compile breaks" sospechados en CourseOfferingMapper/JpaEntity/EnrollmentJpaRepository/StudentRepositoryAdapter eran falsos positivos: ya están migrados.
- No hay migraciones SQL; los renames de columnas requieren `make reset` en dev (destruye volúmenes).
