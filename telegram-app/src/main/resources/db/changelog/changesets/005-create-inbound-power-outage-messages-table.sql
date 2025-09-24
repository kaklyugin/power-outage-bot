-- liquibase formatted sql

-- changeset anton.kaklyugin:005:create-power-outage-inbound-message-table
CREATE TABLE power_outage_inbound_messages
(
    id                     BIGSERIAL NOT NULL,
    city                   TEXT,
    address                TEXT,
    date_time_off          TIMESTAMP WITH TIME ZONE,
    date_time_on           TIMESTAMP WITH TIME ZONE,
    power_outage_reason    TEXT,
    url                    TEXT,
    normalized_street_type TEXT,
    message_hash_code      INTEGER UNIQUE,
    last_updated_at        TIMESTAMP(6),
    CONSTRAINT pk_power_outage_inbound_messages PRIMARY KEY (id)
);

CREATE INDEX idx_power_outage_inbound_message_hash_code
    ON power_outage_inbound_messages(message_hash_code);