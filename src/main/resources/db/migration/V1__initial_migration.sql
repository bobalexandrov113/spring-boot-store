CREATE TABLE addresses
(
    id      INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    street  VARCHAR(255)       NULL,
    city    VARCHAR(255)       NULL,
    zip     VARCHAR(255)       NOT NULL,
    user_id BIGINT             NOT NULL,
    state   VARCHAR(255)       NULL

);

CREATE TABLE categories
(
    id   SMALLINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255)           NULL

);

CREATE TABLE products
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR(255)          NOT NULL,
    description        VARCHAR(255)          NOT NULL,
    price       DECIMAL(10, 2)        NOT NULL,
    category_id SMALLINT               NULL

);

CREATE TABLE profiles
(
    id             BIGINT        PRIMARY KEY,
    bio            VARCHAR(255)  NOT NULL,
    phone_number   VARCHAR(255)  NOT NULL,
    date_of_birth  date          NOT NULL,
    loyalty_points INT DEFAULT 0 NULL

);

CREATE TABLE users
(
    id       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name     VARCHAR(255)          NOT NULL,
    email    VARCHAR(255)          NOT NULL,
    password VARCHAR(255)          NOT NULL
);


ALTER TABLE addresses
    ADD CONSTRAINT addresses_users_id_fk FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

CREATE INDEX addresses_users_id_fk ON addresses (user_id);

ALTER TABLE products
    ADD CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE NO ACTION;

CREATE INDEX fk_category ON products (category_id);

ALTER TABLE profiles
    ADD CONSTRAINT profiles_users_id_fk FOREIGN KEY (id) REFERENCES users (id) ON DELETE NO ACTION;