-- liquibase formatted sql

-- changeset anton.kaklyugin:004:create-power-outage-notifications-table
CREATE TABLE power_outage_notifications
(
    id                  BIGSERIAL NOT NULL,
    user_cart_id        BIGINT,
    notification_text   TEXT,
    is_notified         BOOLEAN DEFAULT FALSE NOT NULL,
    notified_at         TIMESTAMP WITH TIME ZONE,
    message_hash_codes  INTEGER[],
    last_updated_at     TIMESTAMP(6),
    CONSTRAINT pk_power_outage_notifications PRIMARY KEY (id)
);

-- changeset anton.kaklyugin:004:add-foreign-key-constraint-user-cart
ALTER TABLE power_outage_notifications
    ADD CONSTRAINT fk_power_outage_notifications_user_cart
        FOREIGN KEY (user_cart_id) REFERENCES user_cart(id);

-- changeset anton.kaklyugin:004:create-index-on-user-cart-id
CREATE INDEX idx_power_outage_notifications_user_cart_id
    ON power_outage_notifications(user_cart_id);