create table roles
(
    id          uuid PRIMARY KEY,
    name        VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(300),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP,
    created_by  uuid,
    updated_by  uuid
);

create table permissions
(
    id          uuid PRIMARY KEY,
    name        VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(300),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP,
    created_by  uuid,
    updated_by  uuid
);

create table users
(
    id         uuid PRIMARY KEY,
    username   VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    name       VARCHAR(255),
    email      VARCHAR(255),
    is_enabled bool         NOT NULL DEFAULT TRUE,
    is_deleted bool         NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    created_by uuid,
    updated_by uuid
);

create table user_roles
(
    id         uuid PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    created_by uuid,
    updated_by uuid,
    user_id    uuid NOT NULL,
    role_id    uuid NOT NULL,
    constraint fk_ur_user foreign key (user_id) references users (id) on delete cascade,
    constraint fk_ur_role foreign key (role_id) references roles (id),
    constraint fk_ur_create_by foreign key (created_by) references users (id) on delete set null,
    constraint fk_ur_update_by foreign key (updated_by) references users (id) on delete set null,
    constraint uk_user_role unique (user_id, role_id)
);

create table user_permissions
(
    id            uuid PRIMARY KEY,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP,
    created_by    uuid,
    updated_by    uuid,
    user_id       uuid NOT NULL,
    permission_id uuid NOT NULL,
    constraint fk_up_user foreign key (user_id) references users (id),
    constraint fk_up_permission foreign key (permission_id) references permissions (id),
    constraint fk_up_create_by foreign key (created_by) references users (id) on delete set null,
    constraint fk_up_update_by foreign key (updated_by) references users (id) on delete set null,
    constraint uk_user_permission unique (user_id, permission_id)
);
