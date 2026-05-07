DO
$$
    DECLARE
        admin_id uuid := uuidv7();
    BEGIN
        INSERT INTO users(id, username, password, name, email, is_enabled, is_deleted, created_by)
        VALUES (admin_id,
                'admin',
                '$argon2i$v=19$m=64,t=3,p=1$ZE1ZUjlQZUZ5bFEwbVdqRQ$lyo53Yo/K1Q0NbAefdz/kA',
                'Nikolai',
                'admin@admin.by',
                true,
                false,
                admin_id);
        INSERT INTO roles (id, name, description, created_by)
        VALUES (uuidv7(), 'SUPERADMIN', 'superadmin', admin_id),
               (uuidv7(), 'ADMIN', 'admin', admin_id),
               (uuidv7(), 'USER', 'user', admin_id),
               (uuidv7(), 'MANAGER', 'manager', admin_id),
               (uuidv7(), 'PROMOTER', 'promoter', admin_id),
               (uuidv7(), 'CEO', 'ceo', admin_id);
        INSERT INTO permissions(id, name, description, created_by)
        VALUES (uuidv7(), 'READ', 'read data', admin_id),
               (uuidv7(), 'WRITE', 'write data', admin_id),
               (uuidv7(), 'EXECUTE', 'execute data', admin_id),
               (uuidv7(), 'UPDATE', 'update data', admin_id),
               (uuidv7(), 'CREATE', 'createForAdmin data', admin_id);

        INSERT INTO user_roles(id, user_id, role_id, created_by)
        VALUES (uuidv7(), admin_id, (SELECT id FROM roles WHERE name = 'ADMIN'),admin_id),
               (uuidv7(), admin_id, (SELECT id FROM roles WHERE name = 'USER'), admin_id),
               (uuidv7(), admin_id, (SELECT  id FROM roles WHERE name = 'SUPERADMIN'), admin_id);

        INSERT INTO user_permissions(id, user_id, permission_id, created_by)
        VALUES (uuidv7(),
                (SELECT id FROM users WHERE username = 'admin'),
                (SELECT id FROM permissions WHERE name = 'READ'),
                (SELECT id FROM users WHERE username = 'admin')),
               (uuidv7(),
                (SELECT id FROM users WHERE username = 'admin'),
                (SELECT id FROM permissions WHERE name = 'WRITE'),
                (SELECT id FROM users WHERE username = 'admin'));

    END
$$;