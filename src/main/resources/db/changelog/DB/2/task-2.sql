INSERT INTO roles (name, description, created_by)
VALUES ('ADMIN', 'admin', 1),
       ('USER', 'user', 1),
       ('MANAGER', 'manager', 1),
       ('PROMOTER', 'promoter', 1),
       ('CEO', 'ceo', 1);
INSERT INTO permissions(name, description, created_by)
VALUES ('READ', 'read data', 1),
       ('WRITE', 'write data', 1),
       ('EXECUTE', 'execute data', 1),
       ('UPDATE', 'update data', 1),
       ('CREATE', 'createForAdmin data', 1);
INSERT INTO users(username, password, name, email, is_enabled, created_by)
VALUES ('admin',
        '$argon2i$v=19$m=64,t=3,p=1$ZE1ZUjlQZUZ5bFEwbVdqRQ$lyo53Yo/K1Q0NbAefdz/kA',
        'Nikolai',
        'admin@admin.by',
        true,
        1);

INSERT INTO user_roles(user_id, role_id, created_by)
VALUES ((SELECT id FROM users WHERE username = 'admin'), (SELECT id FROM roles WHERE name = 'ADMIN'),
        (SELECT id FROM users WHERE username = 'admin')),
       ((SELECT id FROM users WHERE username = 'admin'), (SELECT id FROM roles WHERE name = 'USER'),
        (SELECT id FROM users WHERE username = 'admin'));


INSERT INTO user_permissions(user_id, permission_id, created_by)
VALUES ((SELECT id FROM users WHERE username = 'admin'),
        (SELECT id FROM permissions WHERE name = 'READ'),
        (SELECT id FROM users WHERE username = 'admin')),
       ((SELECT id FROM users WHERE username = 'admin'),
        (SELECT id FROM permissions WHERE name = 'WRITE'),
        (SELECT id FROM users WHERE username = 'admin'));
