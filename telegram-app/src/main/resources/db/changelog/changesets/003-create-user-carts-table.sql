-- liquibase formatted sql

-- changeset anton.kaklyugin:003:create-user-cart-table
CREATE TABLE user_cart
(
    id                  BIGSERIAL NOT NULL,
    chat_id             BIGINT,
    city_fias_id        VARCHAR(36),
    street              TEXT,
    normalized_street   TEXT,
    last_updated_at     TIMESTAMP(6),
    CONSTRAINT pk_user_cart PRIMARY KEY (id)
);

ALTER TABLE user_cart
    ADD CONSTRAINT fk_user_cart_city
        FOREIGN KEY (city_fias_id) REFERENCES cities(fias_id);

CREATE INDEX idx_user_cart_chat_id
    ON user_cart(chat_id);

CREATE INDEX idx_user_cart_city_fias_id
    ON user_cart(city_fias_id);