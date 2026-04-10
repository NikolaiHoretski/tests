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
        'admin@admin.by', true);

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
