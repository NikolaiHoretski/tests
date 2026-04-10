create table roles
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(300)
);

create table permissions
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(300)
);

create table users
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username   VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    name       VARCHAR(255),
    email      VARCHAR(255),
    is_enabled bool         NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

create table user_roles
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT,
    user_id    BIGINT NOT NULL,
    role_id    BIGINT NOT NULL,
    constraint fk_user foreign key (user_id) references users (id) on delete cascade,
    constraint fk_role foreign key (role_id) references roles (id),
    constraint fk_create_by foreign key (created_by) references users (id) on delete set null,
    constraint fk_update_by foreign key (updated_by) references users (id) on delete set null,
    constraint uk_user_role unique (user_id, role_id)
);

create table role_permissions
(
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by    BIGINT,
    updated_by    BIGINT,
    role_id       BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    constraint fk_role foreign key (role_id) references roles (id),
    constraint fk_permission foreign key (permission_id) references permissions (id),
    constraint fk_create_by foreign key (created_by) references users (id) on delete set null,
    constraint fk_update_by foreign key (updated_by) references users (id) on delete set null,
    constraint uk_role_permission unique (role_id, permission_id)
);
