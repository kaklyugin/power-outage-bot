-- liquibase formatted sql

-- changeset anton.kaklyugin:006:create-state-machines-table
CREATE TABLE state_machines
(
    id                  BIGSERIAL NOT NULL,
    chat_id             BIGINT,
    state               VARCHAR(255),
    state_machine_name  VARCHAR(255),
    last_updated_at     TIMESTAMP(6),
    CONSTRAINT pk_state_machines PRIMARY KEY (id)
);

-- changeset anton.kaklyugin:006:create-index-on-chat-id
CREATE INDEX idx_state_machines_chat_id
    ON state_machines(chat_id);

-- changeset anton.kaklyugin:006:create-index-on-state
CREATE INDEX idx_state_machines_state
    ON state_machines(state);

-- changeset anton.kaklyugin:006:create-index-on-state-machine-name
CREATE INDEX idx_state_machines_state_machine_name
    ON state_machines(state_machine_name);