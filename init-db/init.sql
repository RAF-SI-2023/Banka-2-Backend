CREATE SCHEMA iam_service_schema;
CREATE SCHEMA bank_service_schema;
CREATE SCHEMA notification_service_schema;
CREATE SCHEMA stock_service_schema;
CREATE SCHEMA otc_service_schema;

SET
search_path TO iam_service_schema;
CREATE TABLE IF NOT EXISTS shedlock
(
    name
    VARCHAR
(
    64
) NOT NULL,
    lock_until TIMESTAMP NOT NULL,
    locked_at TIMESTAMP NOT NULL,
    locked_by VARCHAR
(
    255
) NOT NULL,
    PRIMARY KEY
(
    name
)
    );

CREATE TABLE IF NOT EXISTS roles
(
    id
    BIGSERIAL
    PRIMARY
    KEY,
    role_type
    VARCHAR
(
    255
) UNIQUE NOT NULL
    );
INSERT INTO roles (role_type)
VALUES ('ADMIN'),
       ('EMPLOYEE'),
       ('SUPERVISOR'),
       ('AGENT'),
       ('USER') ON CONFLICT (role_type) DO NOTHING;

CREATE TABLE IF NOT EXISTS permissions
(
    id
    BIGSERIAL
    PRIMARY
    KEY,
    permission_type
    VARCHAR
(
    255
) UNIQUE NOT NULL
    );
INSERT INTO permissions (permission_type)
VALUES ('PERMISSION_1'),
       ('PERMISSION_2'),
       ('PERMISSION_3'),
       ('PERMISSION_4'),
       ('PERMISSION_5') ON CONFLICT (permission_type) DO NOTHING;



SET
search_path TO bank_service_schema;
CREATE TABLE IF NOT EXISTS shedlock
(
    name
    VARCHAR
(
    64
) NOT NULL,
    lock_until TIMESTAMP NOT NULL,
    locked_at TIMESTAMP NOT NULL,
    locked_by VARCHAR
(
    255
) NOT NULL,
    PRIMARY KEY
(
    name
)
    );



SET
search_path TO notification_service_schema;
CREATE TABLE IF NOT EXISTS shedlock
(
    name
    VARCHAR
(
    64
) NOT NULL,
    lock_until TIMESTAMP NOT NULL,
    locked_at TIMESTAMP NOT NULL,
    locked_by VARCHAR
(
    255
) NOT NULL,
    PRIMARY KEY
(
    name
)
    );



SET
search_path TO stock_service_schema;
CREATE TABLE IF NOT EXISTS shedlock
(
    name
    VARCHAR
(
    64
) NOT NULL,
    lock_until TIMESTAMP NOT NULL,
    locked_at TIMESTAMP NOT NULL,
    locked_by VARCHAR
(
    255
) NOT NULL,
    PRIMARY KEY
(
    name
)
    );



SET
search_path TO otc_service_schema;
CREATE TABLE IF NOT EXISTS shedlock
(
    name
    VARCHAR
(
    64
) NOT NULL,
    lock_until TIMESTAMP NOT NULL,
    locked_at TIMESTAMP NOT NULL,
    locked_by VARCHAR
(
    255
) NOT NULL,
    PRIMARY KEY
(
    name
)
    );
