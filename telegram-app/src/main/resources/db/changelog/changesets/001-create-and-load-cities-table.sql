-- liquibase formatted sql

-- changeset  anton.kaklyugin:001:create-cities-table
CREATE TABLE cities
(
    fias_id         VARCHAR(36) NOT NULL,
    name            TEXT        NOT NULL,
    type            TEXT        NOT NULL,
    district        TEXT        NOT NULL,
    last_updated_at TIMESTAMP(6) NULL,
    CONSTRAINT pk_cities PRIMARY KEY (fias_id)
);

-- changeset  anton.kaklyugin:001:add-cities-constraints-2
ALTER TABLE cities
    ADD CONSTRAINT chk_cities_fias_id_not_empty CHECK (fias_id <> ''),
ADD CONSTRAINT chk_cities_name_not_empty CHECK (name <> '');

-- changeset  anton.kaklyugin:001:add-cities-index-3
CREATE INDEX idx_cities_name ON cities (name);
CREATE INDEX idx_cities_last_updated ON cities (last_updated_at);

-- changeset  anton.kaklyugin:001:add-cities-comments-4
COMMENT ON TABLE cities IS 'Stores city information with FIAS identifiers';
COMMENT ON COLUMN cities.fias_id IS 'Primary key - FIAS identifier for the city';
COMMENT ON COLUMN cities.name IS 'Name of the city';
COMMENT ON COLUMN cities.last_updated_at IS 'Timestamp of last update for optimistic locking (JPA @Version)';