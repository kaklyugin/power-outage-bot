-- liquibase formatted sql

-- changeset anton.kaklyugin:008:create-location-types-table
CREATE TABLE location_types
(
    id              BIGSERIAL NOT NULL,
    category        TEXT      NOT NULL DEFAULT '',
    type            TEXT      NOT NULL DEFAULT '',
    alias           TEXT      NOT NULL DEFAULT '',
    last_updated_at TIMESTAMP(6),
    CONSTRAINT pk_location_types PRIMARY KEY (id)
);

CREATE INDEX idx_location_types_category ON location_types(category);
CREATE INDEX idx_location_types_type ON location_types(type);
CREATE INDEX idx_location_types_alias ON location_types(alias);
CREATE INDEX idx_location_types_last_updated_at ON location_types(last_updated_at);