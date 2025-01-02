CREATE TABLE IF NOT EXISTS client
(
    id           UUID                NOT NULL,
    first_name   VARCHAR(200)        NOT NULL,
    last_name    VARCHAR(200)        NOT NULL,
    phone_number VARCHAR(50) UNIQUE  NOT NULL,
    email        VARCHAR(255) UNIQUE NOT NULL,
    CONSTRAINT pk_client PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS orders
(
    id                UUID           NOT NULL,
    street            VARCHAR(255)   NOT NULL,
    postcode          VARCHAR(50)    NOT NULL,
    city              VARCHAR(100)   NOT NULL,
    country           VARCHAR(100)   NOT NULL,
    address_number    INT,
    number_of_pilotes INT            NOT NULL,
    total_price       DECIMAL(10, 2) NOT NULL,
    creation_time     TIMESTAMP,
    update_time       TIMESTAMP,
    client_id         UUID,
    cancelled         BOOLEAN        NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_orders PRIMARY KEY (id)
);

CREATE INDEX idx_client_first_name ON client (first_name);

CREATE INDEX idx_client_last_name ON client (last_name);

ALTER TABLE orders
    ADD CONSTRAINT fk_orders_client FOREIGN KEY (client_id) REFERENCES client (id);
