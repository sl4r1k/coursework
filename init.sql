CREATE DATABASE user_service;

CREATE TABLE roles (
    id    SERIAL PRIMARY KEY,
    title VARCHAR(255) UNIQUE NOT NULL
);

CREATE TYPE privilege AS ENUM (
    'VENDORS_READ_PRIVILEGE',
    'VENDORS_WRITE_PRIVILEGE',
    'SUPPLIERS_READ_PRIVILEGE',
    'SUPPLIERS_WRITE_PRIVILEGE',
    'USER_GROUPS_READ_PRIVILEGE',
    'USER_GROUPS_WRITE_PRIVILEGE',
    'USER_SEGMENTS_READ_PRIVILEGE',
    'USER_SEGMENTS_WRITE_PRIVILEGE',
    'USERS_READ_PRIVILEGE',
    'USERS_WRITE_PRIVILEGE',
    'MESSAGES_READ_PRIVILEGE',
    'MESSAGES_WRITE_PRIVILEGE'
);

CREATE TYPE grant_type AS ENUM (
    'SELECT',
    'INSERT',
    'UPDATE',
    'DELETE',
    'ALL'
);

CREATE TABLE privileges_tables_grants (
    privilege   PRIVILEGE,
    table_title VARCHAR,
    grant_type  GRANT_TYPE,
    PRIMARY KEY (privilege, table_title, grant_type)
);

CREATE TABLE roles_privileges (
    role_id   INTEGER REFERENCES roles (id),
    privilege PRIVILEGE,
    PRIMARY KEY (role_id, privilege)
);

CREATE TYPE account_type AS ENUM (
    'PHYSICAL_PERSON',
    'LEGAL_PERSON'
);

CREATE TABLE users (
    id                         SERIAL PRIMARY KEY,
    login                      VARCHAR(255) UNIQUE           NOT NULL,
    password                   VARCHAR(255)                  NOT NULL,
    account_type               account_type,
    last_name                  VARCHAR(255)                  NOT NULL,
    first_name                 VARCHAR(255)                  NOT NULL,
    company_name               VARCHAR(255),
    role_id                    INTEGER REFERENCES roles (id) NOT NULL,
    phone_number               VARCHAR(15) UNIQUE            NOT NULL,
    fax_number                 VARCHAR(255) UNIQUE,
    email_address              VARCHAR(255) UNIQUE,
    url_address                VARCHAR,
    individual_taxpayer_number VARCHAR(12) UNIQUE            NOT NULL,
    bank_card_number           VARCHAR(16) UNIQUE            NOT NULL,
    vendor_commission          DECIMAL,
    is_fixed_commission        BOOLEAN,
    vendor_balance             DECIMAL(19, 4),
    postal_code                VARCHAR(12)                   NOT NULL,
    country                    VARCHAR(255)                  NOT NULL,
    region                     VARCHAR(255)                  NOT NULL,
    city                       VARCHAR(255)                  NOT NULL,
    street                     VARCHAR(255)                  NOT NULL,
    house_number               VARCHAR(255)                  NOT NULL,
    note                       TEXT,
    last_login                 TIMESTAMP,
    registration_date          TIMESTAMP                     NOT NULL,
    availability               VARCHAR                       NOT NULL
);

CREATE INDEX users_last_name_first_name_idx ON users (last_name, first_name);

CREATE INDEX users_company_name_idx ON users (company_name);

CREATE VIEW users_tables_grants AS (
    SELECT
        u.id AS user_id,
        ptg.table_title,
        ptg.grant_type
    FROM
        privileges_tables_grants AS ptg
        INNER JOIN roles_privileges AS rp ON ptg.privilege = rp.privilege
        INNER JOIN roles AS r ON rp.role_id = r.id
        INNER JOIN users AS u ON r.id = u.role_id
);

CREATE OR REPLACE FUNCTION create_database_user_by_role() RETURNS trigger AS $$
DECLARE
    customer_role_id INTEGER := (
        SELECT id
        FROM roles
        WHERE title = 'ROLE_CUSTOMER'
    );
    rec RECORD;
BEGIN
    IF ((NEW.role_id <> customer_role_id) AND ((OLD IS NULL) OR (OLD.role_id <> NEW.role_id))) THEN
        EXECUTE format(
            'CREATE USER %s WITH LOGIN PASSWORD ''%s'';',
            new.login, new.password
        );
        FOR rec IN (
            SELECT table_title, ARRAY_AGG(grant_type) AS grants
            FROM users_tables_grants
            WHERE user_id = NEW.id
            GROUP BY table_title)
        LOOP
            EXECUTE format(
                'GRANT %s ON TABLE %s TO %s;',
                array_to_string(rec.grants, ', '), rec.table_title, new.login
            );
        END LOOP;
    END IF;
    IF ((NEW.login <> OLD.login) OR (NEW.password <> OLD.password)) THEN
        EXECUTE format(
            'ALTER USER %s RENAME TO %s; '
                || 'ALTER USER %s WITH PASSWORD ''%s'';',
            OLD.login, NEW.login, NEW.login, NEW.password
        );
    END IF;
    RETURN NEW;
END
$$ LANGUAGE plpgsql;

CREATE TRIGGER database_user_creation_by_role
    AFTER INSERT OR UPDATE
    ON users
    FOR EACH ROW
EXECUTE PROCEDURE create_database_user_by_role();

CREATE OR REPLACE FUNCTION delete_database_user_by_role() RETURNS trigger AS $$
BEGIN
    EXECUTE format(
                'REVOKE ALL PRIVILEGES ON ALL TABLES IN SCHEMA %s FROM %s;'
                || ' DROP USER %s',
                current_schema(), old.login, old.login
        );
    RETURN OLD;
END
$$ LANGUAGE plpgsql;

CREATE TRIGGER database_user_deletion_by_role
    AFTER DELETE
    ON users
    FOR EACH ROW
EXECUTE PROCEDURE delete_database_user_by_role();

CREATE TABLE user_tags (
    id      SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users (id) NOT NULL,
    text    VARCHAR(255)                  NOT NULL
);

CREATE TABLE user_ordered_products (
    id         SERIAL PRIMARY KEY,
    user_id    INTEGER REFERENCES users (id),
    product_id INTEGER,
    price      DECIMAL(19, 4),
    date       TIMESTAMP
);

CREATE TABLE vendors_merchandises (
    vendor_id      INTEGER REFERENCES users (id),
    merchandise_id INTEGER,
    PRIMARY KEY (vendor_id, merchandise_id)
);

CREATE TABLE vendors_orders (
    vendor_id INTEGER REFERENCES users (id),
    order_id  INTEGER,
    PRIMARY KEY (vendor_id, order_id)
);

CREATE TABLE suppliers_products (
    supplier_id INTEGER REFERENCES users (id),
    product_id  INTEGER,
    PRIMARY KEY (supplier_id, product_id)
);

CREATE TABLE user_groups (
    id           SERIAL PRIMARY KEY,
    title        VARCHAR(255) UNIQUE           NOT NULL,
    role_id      INTEGER REFERENCES roles (id) NOT NULL,
    availability VARCHAR                       NOT NULL
);

CREATE TABLE user_groups_users (
    group_id INTEGER REFERENCES user_groups (id),
    user_id  INTEGER REFERENCES users (id),
    PRIMARY KEY (group_id, user_id)
);

CREATE TABLE user_segments (
    id                         SERIAL PRIMARY KEY,
    title                      VARCHAR(255) UNIQUE,
    availability               VARCHAR NOT NULL,
    is_every_condition_matched BOOLEAN NOT NULL
);

CREATE TABLE user_segments_users (
    segment_id INTEGER REFERENCES user_segments (id),
    user_id    INTEGER REFERENCES users (id),
    PRIMARY KEY (segment_id, user_id)
);

CREATE TABLE user_segment_filters (
    id         SERIAL PRIMARY KEY,
    segment_id INTEGER REFERENCES user_segments (id) NOT NULL,
    field      VARCHAR                               NOT NULL,
    condition  VARCHAR                               NOT NULL,
    UNIQUE (segment_id, field)
);

CREATE TABLE user_segment_filter_values (
    id        SERIAL PRIMARY KEY,
    filter_id INTEGER REFERENCES user_segment_filters (id) NOT NULL,
    value     TEXT                                         NOT NULL
);

CREATE TABLE user_search_criteria (
    id                         SERIAL PRIMARY KEY,
    title                      VARCHAR(255) UNIQUE NOT NULL,
    login                      VARCHAR(255),
    account_type               VARCHAR,
    full_name                  VARCHAR,
    company_name               VARCHAR(255),
    role_id                    INTEGER REFERENCES roles (id),
    phone_number               VARCHAR(15),
    fax_number                 VARCHAR(255),
    email_address              VARCHAR(255),
    url_address                VARCHAR,
    individual_taxpayer_number VARCHAR(12),
    bank_card_number           VARCHAR(16),
    postal_code                VARCHAR(255),
    country                    VARCHAR(255),
    region                     VARCHAR(255),
    city                       VARCHAR(255),
    street                     VARCHAR(255),
    house_number               VARCHAR(255),
    begin_registration_date    TIMESTAMP,
    end_registration_date      TIMESTAMP
);

CREATE TABLE user_search_criteria_user_tags (
    criteria_id INTEGER REFERENCES user_search_criteria (id),
    tag_id      INTEGER REFERENCES user_tags (id),
    PRIMARY KEY (criteria_id, tag_id)
);

CREATE TABLE user_search_criteria_ordered_products (
    criteria_id INTEGER REFERENCES users (id),
    product_id  INTEGER,
    PRIMARY KEY (criteria_id, product_id)
);

CREATE TABLE user_search_criteria_user_groups (
    criteria_id INTEGER REFERENCES user_search_criteria (id),
    group_id    INTEGER REFERENCES user_groups (id),
    PRIMARY KEY (criteria_id, group_id)
);

CREATE TABLE user_search_criteria_user_segments (
    criteria_id INTEGER REFERENCES user_search_criteria (id),
    segment_id  INTEGER REFERENCES user_segments (id),
    PRIMARY KEY (criteria_id, segment_id)
);

CREATE TABLE vendor_search_criteria (
    id            SERIAL PRIMARY KEY,
    title         VARCHAR(255) UNIQUE NOT NULL,
    full_name     VARCHAR,
    company_name  VARCHAR(255),
    phone_number  VARCHAR(15),
    fax_number    VARCHAR(255),
    email_address VARCHAR(255),
    url_address   VARCHAR
);

CREATE TABLE vendor_search_criteria_vendor_merchandises (
    criteria_id    INTEGER REFERENCES vendor_search_criteria (id),
    merchandise_id INTEGER,
    PRIMARY KEY (criteria_id, merchandise_id)
);

CREATE TABLE vendor_search_criteria_vendor_orders (
    criteria_id INTEGER REFERENCES vendor_search_criteria (id),
    order_id    INTEGER,
    PRIMARY KEY (criteria_id, order_id)
);

CREATE TABLE supplier_search_criteria (
    id            SERIAL PRIMARY KEY,
    title         VARCHAR(255) UNIQUE NOT NULL,
    full_name     VARCHAR,
    company_name  VARCHAR(255),
    phone_number  VARCHAR(15),
    fax_number    VARCHAR(255),
    email_address VARCHAR(255),
    url_address   VARCHAR
);

CREATE TABLE supplier_search_criteria_products (
    criteria_id INTEGER REFERENCES supplier_search_criteria (id),
    product_id  INTEGER,
    PRIMARY KEY (criteria_id, product_id)
);

CREATE OR REPLACE FUNCTION to_rgx(s varchar) RETURNS varchar AS $$
BEGIN
    RETURN concat('%', s, '%');
END $$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION array_to_rgxs(strs varchar) RETURNS varchar[] AS $$
BEGIN
    FOR i IN 1..array_length(strs, 1) LOOP
        strs[i] := to_rgx(strs[i]);
    END LOOP;
END $$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION compare(s1 varchar, s2 varchar) RETURNS boolean AS $$
BEGIN
    RETURN s1 ~~* to_rgx(s2);
END $$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION compare_with_array(s varchar, VARIADIC strs varchar[]) RETURNS boolean AS $$
BEGIN
    RETURN unnest(array_to_rgxs(strs)) ~~* s;
END $$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION compare_by_full_text(s1 varchar, s2 varchar) RETURNS boolean AS $$
DECLARE
    strs VARCHAR[];
BEGIN
    strs := regexp_split_to_array(s2, '\s+');
    FOR i IN 1..array_length(strs, 1) LOOP
        IF s1 NOT ILIKE to_rgx(strs[i]) THEN
            RETURN false;
        END IF;
    END LOOP;
    RETURN true;
END $$ LANGUAGE plpgsql;

CREATE TABLE vendor_transactions (
    id        SERIAL PRIMARY KEY,
    vendor_id INTEGER REFERENCES users (id) NOT NULL,
    amount    DECIMAL(19, 4)                NOT NULL,
    type      VARCHAR                       NOT NULL,
    status    VARCHAR                       NOT NULL,
    date      TIMESTAMP                     NOT NULL,
    comment   TEXT
);

CREATE TABLE messages (
    id      SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users (id) NOT NULL,
    text    TEXT                          NOT NULL,
    date    TIMESTAMP                     NOT NULL
);

CREATE TABLE messages_documents (
    message_id  INTEGER REFERENCES messages (id),
    document_id INTEGER,
    PRIMARY KEY (message_id, document_id)
);

CREATE TABLE vendor_communications (
    id          SERIAL PRIMARY KEY,
    vendor_id   INTEGER REFERENCES users (id) NOT NULL,
    customer_id INTEGER REFERENCES users (id) NOT NULL,
    product_id  INTEGER                       NOT NULL
);

CREATE TABLE vendor_communications_messages (
    communication_id INTEGER REFERENCES vendor_communications (id),
    message_id       INTEGER REFERENCES messages (id),
    PRIMARY KEY (communication_id, message_id)
);

CREATE TABLE dialogs (
    id             SERIAL PRIMARY KEY,
    first_user_id  INTEGER REFERENCES users (id) NOT NULL,
    second_user_id INTEGER REFERENCES users (id) NOT NULL,
    UNIQUE (first_user_id, second_user_id)
);

CREATE TABLE dialogs_messages (
    dialog_id  INTEGER REFERENCES dialogs (id),
    message_id INTEGER REFERENCES messages (id),
    PRIMARY KEY (dialog_id, message_id)
);

CREATE TABLE email_templates (
    id      SERIAL PRIMARY KEY,
    event   VARCHAR NOT NULL,
    subject TEXT    NOT NULL,
    text    TEXT    NOT NULL
);
