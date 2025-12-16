INSERT INTO permission (resource, operation, scope, description)
VALUES ('STUDENT', 'CREATE', 'ALL', 'Crear estudiantes'),
       ('STUDENT', 'READ', 'ALL', 'Ver todos los estudiantes'),
       ('STUDENT', 'READ', 'SELF', 'Ver solo su propio perfil de estudiante'),
       ('STUDENT', 'UPDATE', 'ALL', 'Actualizar cualquier estudiante'),
       ('STUDENT', 'UPDATE', 'SELF', 'Actualizar su propio perfil de estudiante'),
       ('STUDENT', 'DELETE', 'ALL', 'Eliminar estudiantes'),

       ('ENROLLMENT', 'CREATE', 'ALL', 'Crear inscripciones para cualquier estudiante'),
       ('ENROLLMENT', 'CREATE', 'SELF', 'Crear sus propias inscripciones'),
       ('ENROLLMENT', 'READ', 'ALL', 'Ver todas las inscripciones'),
       ('ENROLLMENT', 'READ', 'SELF', 'Ver solo sus propias inscripciones'),
       ('ENROLLMENT', 'UPDATE', 'ALL', 'Actualizar cualquier inscripción'),
       ('ENROLLMENT', 'UPDATE', 'SELF', 'Actualizar sus propias inscripciones'),
       ('ENROLLMENT', 'DELETE', 'ALL', 'Eliminar cualquier inscripción'),
       ('ENROLLMENT', 'DELETE', 'SELF', 'Eliminar sus propias inscripciones'),

       ('UI_VIEW', 'CREATE', 'ALL', 'Crear nuevas vistas en el sistema'),
       ('UI_VIEW', 'READ', 'ALL', 'Ver configuración de vistas'),
       ('UI_VIEW', 'UPDATE', 'ALL', 'Actualizar configuración de vistas'),
       ('UI_VIEW', 'DELETE', 'ALL', 'Eliminar vistas del sistema');

INSERT INTO ui_view (code, route, label, module, sort_order, active)
VALUES ('MY_PROFILE', '/app/profile', 'Mi perfil', 'ACCOUNT', 1, true),
       ('MY_ENROLLMENTS', '/app/enrollment/my', 'Mis inscripciones', 'ENROLLMENT', 1, true),

       ('STUDENT_LIST', '/app/students', 'Estudiantes', 'ADMIN', 1, true),
       ('ENROLLMENT_ADMIN', '/app/enrollment', 'Gestión de inscripciones', 'ADMIN', 2, true),
       ('FACULTY_LIST', '/app/faculty', 'Facultades', 'ADMIN', 3, true),
       ('CAREER_LIST', '/app/career', 'Carreras', 'ADMIN', 4, true),
       ('COURSE_LIST', '/app/course', 'Cursos', 'ADMIN', 5, true),
       ('TERM_LIST', '/app/term', 'Vigencias', 'ADMIN', 6, true),
       ('COURSE_OFFERING_LIST', '/app/course-offering', 'Cursos en Vigencia', 'ADMIN', 7, true),

       ('RBAC_ROLES', '/app/rbac/roles', 'Gestión de Roles', 'RBAC', 1, true),
       ('RBAC_USERS', '/app/rbac/users', 'Asignar Roles a Usuarios', 'RBAC', 2, true),
       ('RBAC_PERMISSIONS', '/app/rbac/permissions', 'Catálogo de Permisos', 'RBAC', 3, true);

INSERT INTO role (name, description)
VALUES ('ADMIN', 'Administrador con acceso completo al sistema'),
       ('STAFF', 'Personal administrativo'),
       ('STUDENT', 'Estudiante');


INSERT INTO role_permission (role_id, permission_id)
SELECT (SELECT id FROM role WHERE name = 'ADMIN'), id
FROM permission;

INSERT INTO role_permission (role_id, permission_id)
SELECT (SELECT id FROM role WHERE name = 'STAFF'), id
FROM permission
WHERE resource = 'STUDENT'
  AND operation IN ('READ', 'UPDATE')
  AND scope = 'ALL';

INSERT INTO role_permission (role_id, permission_id)
SELECT (SELECT id FROM role WHERE name = 'STAFF'), id
FROM permission
WHERE resource = 'ENROLLMENT'
  AND scope = 'ALL';

INSERT INTO role_permission (role_id, permission_id)
SELECT (SELECT id FROM role WHERE name = 'STAFF'), id
FROM permission
WHERE resource = 'UI_VIEW'
  AND operation = 'READ'
  AND scope = 'ALL';

INSERT INTO role_permission (role_id, permission_id)
SELECT (SELECT id FROM role WHERE name = 'STUDENT'), id
FROM permission
WHERE resource IN ('STUDENT', 'ENROLLMENT')
  AND operation IN ('READ', 'UPDATE', 'CREATE', 'DELETE')
  AND scope = 'SELF';


INSERT INTO role_view (role_id, view_code)
SELECT (SELECT id FROM role WHERE name = 'ADMIN'), code
FROM ui_view
WHERE active = true;

INSERT INTO role_view (role_id, view_code)
SELECT (SELECT id FROM role WHERE name = 'STAFF'), code
FROM ui_view
WHERE code IN (
               'MY_PROFILE',
               'STUDENT_LIST',
               'ENROLLMENT_ADMIN',
               'COURSE_LIST',
               'COURSE_OFFERING_LIST',
               'TERM_LIST',
               'FACULTY_LIST'
    );

INSERT INTO role_view (role_id, view_code)
SELECT (SELECT id FROM role WHERE name = 'STUDENT'), code
FROM ui_view
WHERE code IN ('MY_PROFILE', 'MY_ENROLLMENTS');

INSERT INTO "user" (email, full_name, password, two_factor_enabled, username)
VALUES ('admin@gmail.com', 'Admin Principal',
        '$2a$10$xDq5cPo.TscLOq7UbRgzeu/c.hgXXDrKZNrRKyXgdAjLGBSGpcrzK', false, 'admin123'),
       ('staff@gmail.com', 'Personal Administrativo',
        '$2a$10$xDq5cPo.TscLOq7UbRgzeu/c.hgXXDrKZNrRKyXgdAjLGBSGpcrzK', false, 'staff123'),
       ('student@gmail.com', 'Juan Pérez',
        '$2a$10$xDq5cPo.TscLOq7UbRgzeu/c.hgXXDrKZNrRKyXgdAjLGBSGpcrzK', false, 'student123');

INSERT INTO user_role (user_id, role_id)
SELECT (SELECT id FROM "user" WHERE username='admin123'), (SELECT id FROM role WHERE name='ADMIN');

INSERT INTO user_role (user_id, role_id)
SELECT (SELECT id FROM "user" WHERE username='staff123'), (SELECT id FROM role WHERE name='STAFF');

INSERT INTO user_role (user_id, role_id)
SELECT (SELECT id FROM "user" WHERE username='student123'), (SELECT id FROM role WHERE name='STUDENT');



-- ============================================
-- QUERIES DE VERIFICACIÓN
-- ============================================

-- 1. Ver todos los permisos creados
SELECT id,
       CONCAT(resource, ':', operation, ':', scope) as permission_string,
       description
FROM permission
ORDER BY resource, operation, scope;

-- 2. Ver todas las vistas creadas
SELECT code, route, label, module, sort_order, active
FROM ui_view
ORDER BY module, sort_order;

-- 3. Ver roles con conteo de permisos y vistas
SELECT r.id,
       r.name,
       r.description,
       COUNT(DISTINCT rp.permission_id) as total_permisos,
       COUNT(DISTINCT rv.view_code)     as total_vistas
FROM role r
         LEFT JOIN role_permission rp ON r.id = rp.role_id
         LEFT JOIN role_view rv ON r.id = rv.role_id
GROUP BY r.id, r.name, r.description
ORDER BY r.id;

-- 4. Ver permisos del rol ADMIN
SELECT r.name                                             as rol,
       CONCAT(p.resource, ':', p.operation, ':', p.scope) as permiso,
       p.description
FROM role r
         JOIN role_permission rp ON r.id = rp.role_id
         JOIN permission p ON rp.permission_id = p.id
WHERE r.name = 'ADMIN'
ORDER BY p.resource, p.operation;

-- 5. Ver vistas del rol STAFF
SELECT r.name as rol,
       v.code,
       v.route,
       v.label,
       v.module
FROM role r
         JOIN role_view rv ON r.id = rv.role_id
         JOIN ui_view v ON rv.view_code = v.code
WHERE r.name = 'STAFF'
ORDER BY v.module, v.sort_order;

-- 6. Ver usuarios con sus roles completos
SELECT u.id,
       u.username,
       u.email,
       u.full_name,
       STRING_AGG(r.name, ', ')         as roles,
       COUNT(DISTINCT rp.permission_id) as total_permisos,
       COUNT(DISTINCT rv.view_code)     as total_vistas
FROM "user" u
         LEFT JOIN user_role ur ON u.id = ur.user_id
         LEFT JOIN role r ON ur.role_id = r.id
         LEFT JOIN role_permission rp ON r.id = rp.role_id
         LEFT JOIN role_view rv ON r.id = rv.role_id
GROUP BY u.id, u.username, u.email, u.full_name
ORDER BY u.username;

-- 7. Ver permisos específicos de un usuario (admin123)
SELECT DISTINCT CONCAT(p.resource, ':', p.operation, ':', p.scope) as permission_string
FROM "user" u
         JOIN user_role ur ON u.id = ur.user_id
         JOIN role r ON ur.role_id = r.id
         JOIN role_permission rp ON r.id = rp.role_id
         JOIN permission p ON rp.permission_id = p.id
WHERE u.username = 'admin123'
ORDER BY permission_string;

-- 8. Ver vistas específicas de un usuario (staff123)
SELECT DISTINCT v.code,
                v.route,
                v.label,
                v.module,
                v.sort_order
FROM "user" u
         JOIN user_role ur ON u.id = ur.user_id
         JOIN role r ON ur.role_id = r.id
         JOIN role_view rv ON r.id = rv.role_id
         JOIN ui_view v ON rv.view_code = v.code
WHERE u.username = 'staff123'
ORDER BY v.module, v.sort_order;


INSERT INTO role_permission (role_id, permission_id)
SELECT (SELECT id FROM role WHERE name = 'STAFF'),
       (SELECT id
        FROM permission
        WHERE resource = 'UI_VIEW'
          AND operation = 'READ'
          AND scope = 'ALL')
ON CONFLICT DO NOTHING;
