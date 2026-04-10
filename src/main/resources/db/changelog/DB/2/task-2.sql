INSERT INTO roles (name, description)
VALUES ('ADMIN', 'admin'),
       ('USER', 'user'),
       ('MANAGER', 'manager'),
       ('PROMOTER', 'promoter'),
       ('CEO', 'ceo');
INSERT INTO permissions(name, description)
VALUES ('READ', 'read data'),
       ('WRITE', 'write data'),
       ('EXECUTE', 'execute data'),
       ('UPDATE', 'update data'),
       ('CREATE', 'create data');
INSERT INTO users(username, password, name, email, is_enabled)
VALUES ('admin', '$argon2i$v=19$m=64,t=3,p=1$ZE1ZUjlQZUZ5bFEwbVdqRQ$lyo53Yo/K1Q0NbAefdz/kA', 'Nikolai',
        'admin@admin.by', true),
       ('user_test', '$argon2i$v=19$m=64,t=3,p=1$ZE1ZUjlQZUZ5bFEwbVdqRQ$lyo53Yo/K1Q0NbAefdz/kA', 'Ivan', 'user@test.by',
        true),
       ('manager_bob', '$argon2i$v=19$m=64,t=3,p=1$ZE1ZUjlQZUZ5bFEwbVdqRQ$lyo53Yo/K1Q0NbAefdz/kA', 'Bob', 'bob@work.by',
        true),
       ('promo_girl', '$argon2i$v=19$m=64,t=3,p=1$ZE1ZUjlQZUZ5bFEwbVdqRQ$lyo53Yo/K1Q0NbAefdz/kA', 'Anna',
        'anna@promo.by', true),
       ('big_boss', '$argon2i$v=19$m=64,t=3,p=1$ZE1ZUjlQZUZ5bFEwbVdqRQ$lyo53Yo/K1Q0NbAefdz/kA', 'Dmitry',
        'ceo@company.by', true);

INSERT INTO user_roles(user_id, role_id, created_by)
VALUES ((SELECT id FROM users WHERE username = 'admin'), (SELECT id FROM roles WHERE name = 'ADMIN'),
        (SELECT id FROM users WHERE username = 'admin')),
       ((SELECT id FROM users WHERE username = 'admin'), (SELECT id FROM roles WHERE name = 'USER'),
        (SELECT id FROM users WHERE username = 'admin'));

INSERT INTO user_roles(user_id, role_id, created_by)
VALUES ((SELECT id FROM users WHERE username = 'user_test'), (SELECT id FROM roles WHERE name = 'USER'),
        (SELECT id FROM users WHERE username = 'admin'));

INSERT INTO user_roles(user_id, role_id, created_by)
VALUES ((SELECT id FROM users WHERE username = 'manager_bob'), (SELECT id FROM roles WHERE name = 'MANAGER'),
        (SELECT id FROM users WHERE username = 'admin')),
       ((SELECT id FROM users WHERE username = 'manager_bob'), (SELECT id FROM roles WHERE name = 'USER'),
        (SELECT id FROM users WHERE username = 'admin'));

INSERT INTO user_roles(user_id, role_id, created_by)
VALUES ((SELECT id FROM users WHERE username = 'promo_girl'), (SELECT id FROM roles WHERE name = 'PROMOTER'),
        (SELECT id FROM users WHERE username = 'admin'));

INSERT INTO user_roles(user_id, role_id, created_by)
VALUES ((SELECT id FROM users WHERE username = 'big_boss'), (SELECT id FROM roles WHERE name = 'CEO'),
        (SELECT id FROM users WHERE username = 'admin')),
       ((SELECT id FROM users WHERE username = 'big_boss'), (SELECT id FROM roles WHERE name = 'ADMIN'),
        (SELECT id FROM users WHERE username = 'admin'));


INSERT INTO role_permissions(role_id, permission_id, created_by)
VALUES ((SELECT id FROM roles WHERE name = 'ADMIN'), (SELECT id FROM permissions WHERE name = 'READ'),
        (SELECT id FROM users WHERE username = 'admin')),
       ((SELECT id FROM roles WHERE name = 'ADMIN'), (SELECT id FROM permissions WHERE name = 'WRITE'),
        (SELECT id FROM users WHERE username = 'admin')),
       ((SELECT id FROM roles WHERE name = 'ADMIN'), (SELECT id FROM permissions WHERE name = 'UPDATE'),
        (SELECT id FROM users WHERE username = 'admin'));

INSERT INTO role_permissions(role_id, permission_id, created_by)
VALUES ((SELECT id FROM roles WHERE name = 'USER'), (SELECT id FROM permissions WHERE name = 'READ'),
        (SELECT id FROM users WHERE username = 'admin'));

INSERT INTO role_permissions(role_id, permission_id, created_by)
VALUES ((SELECT id FROM roles WHERE name = 'MANAGER'), (SELECT id FROM permissions WHERE name = 'READ'),
        (SELECT id FROM users WHERE username = 'admin')),
       ((SELECT id FROM roles WHERE name = 'MANAGER'), (SELECT id FROM permissions WHERE name = 'CREATE'),
        (SELECT id FROM users WHERE username = 'admin')),
       ((SELECT id FROM roles WHERE name = 'MANAGER'), (SELECT id FROM permissions WHERE name = 'UPDATE'),
        (SELECT id FROM users WHERE username = 'admin'));

INSERT INTO role_permissions(role_id, permission_id, created_by)
VALUES ((SELECT id FROM roles WHERE name = 'PROMOTER'), (SELECT id FROM permissions WHERE name = 'READ'),
        (SELECT id FROM users WHERE username = 'admin')),
       ((SELECT id FROM roles WHERE name = 'PROMOTER'), (SELECT id FROM permissions WHERE name = 'EXECUTE'),
        (SELECT id FROM users WHERE username = 'admin'));

INSERT INTO role_permissions(role_id, permission_id, created_by)
SELECT (SELECT id FROM roles WHERE name = 'CEO'), id, (SELECT id FROM users WHERE username = 'admin')
FROM permissions;
