-- liquibase formatted sql

-- changeset anton.kaklyugin:002:create-streets-table
CREATE TABLE streets (
                         id SERIAL PRIMARY KEY,
                         type VARCHAR(255) NULL,
                         city_fias_id VARCHAR(255) NOT NULL,
                         name TEXT NOT NULL,
                         last_updated_at TIMESTAMP(6) NULL,
                         CONSTRAINT fk_streets_city FOREIGN KEY (city_fias_id)
                             REFERENCES cities(fias_id) ON DELETE CASCADE
);

CREATE INDEX idx_streets_city_fias_id ON streets(city_fias_id);
CREATE INDEX idx_streets_name ON streets(name);
CREATE INDEX idx_streets_type ON streets(type);

COMMENT ON TABLE streets IS 'Stores street information linked to cities';
COMMENT ON COLUMN streets.id IS 'Auto-generated primary key';
COMMENT ON COLUMN streets.type IS 'Type of street (e.g., street, avenue, boulevard)';
COMMENT ON COLUMN streets.city_fias_id IS 'Foreign key reference to cities table (FIAS ID)';
COMMENT ON COLUMN streets.name IS 'Name of the street';
COMMENT ON COLUMN streets.last_updated_at IS 'Timestamp of last update for optimistic locking (JPA @Version)';