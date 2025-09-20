-- liquibase formatted sql

-- changeset anton.kaklyugin:005:create-power-outage-inbound-message-table
CREATE TABLE power_outage_inbound_message
(
    id                   BIGSERIAL NOT NULL,
    city                 TEXT,
    address              TEXT,
    date_time_off        TIMESTAMP WITH TIME ZONE,
    date_time_on         TIMESTAMP WITH TIME ZONE,
    power_outage_reason  TEXT,
    url                  TEXT,
    message_hash_code    INTEGER UNIQUE,
    last_updated_at      TIMESTAMP(6),
    CONSTRAINT pk_power_outage_inbound_message PRIMARY KEY (id)
);

-- changeset anton.kaklyugin:005:create-index-on-message-hash-code
CREATE INDEX idx_power_outage_inbound_message_hash_code
    ON power_outage_inbound_message(message_hash_code);