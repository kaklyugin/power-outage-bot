-- liquibase formatted sql

-- changeset anton.kaklyugin:002:create-address-table
CREATE TABLE streets (
                                fias_id VARCHAR(36) PRIMARY KEY,
                                city_fias_id VARCHAR(36) NULL,
                                city_with_type TEXT NULL,
                                city_type VARCHAR(255) NULL,
                                city_type_full TEXT NULL,
                                city TEXT NULL,
                                settlement_fias_id VARCHAR(36) NULL,
                                settlement_with_type TEXT NULL,
                                settlement_type VARCHAR(255) NULL,
                                settlement_type_full VARCHAR(255) NULL,
                                settlement TEXT NULL,
                                street_fias_id VARCHAR(36) NULL,
                                street_with_type TEXT NULL,
                                street_type VARCHAR(255) NULL,
                                street_type_full VARCHAR(255) NULL,
                                street TEXT NULL,
                                last_updated_at TIMESTAMP(6) NULL
);

CREATE INDEX idx_streets_city_fias_id ON streets(city_fias_id);
CREATE INDEX idx_streets_settlement_fias_id ON streets(settlement_fias_id);
CREATE INDEX idx_streets_street_fias_id ON streets(street_fias_id);
CREATE INDEX idx_streets_city ON streets(city);
CREATE INDEX idx_streets_settlement ON streets(settlement);
CREATE INDEX idx_streets_street ON streets(street);

COMMENT ON TABLE streets IS 'Stores comprehensive address information with FIAS identifiers';
COMMENT ON COLUMN streets.fias_id IS 'Primary key - FIAS identifier for the address record';
COMMENT ON COLUMN streets.city_fias_id IS 'FIAS identifier for the city';
COMMENT ON COLUMN streets.city_with_type IS 'City name with type (e.g., "г. Москва")';
COMMENT ON COLUMN streets.city_type IS 'Abbreviated city type (e.g., "г")';
COMMENT ON COLUMN streets.city_type_full IS 'Full city type (e.g., "город")';
COMMENT ON COLUMN streets.city IS 'City name without type (e.g., "Москва")';
COMMENT ON COLUMN streets.settlement_fias_id IS 'FIAS identifier for the settlement';
COMMENT ON COLUMN streets.settlement_with_type IS 'Settlement name with type';
COMMENT ON COLUMN streets.settlement_type IS 'Abbreviated settlement type';
COMMENT ON COLUMN streets.settlement_type_full IS 'Full settlement type';
COMMENT ON COLUMN streets.settlement IS 'Settlement name without type';
COMMENT ON COLUMN streets.street_fias_id IS 'FIAS identifier for the street';
COMMENT ON COLUMN streets.street_with_type IS 'Street name with type (e.g., "ул. Ленина")';
COMMENT ON COLUMN streets.street_type IS 'Abbreviated street type (e.g., "ул")';
COMMENT ON COLUMN streets.street_type_full IS 'Full street type (e.g., "улица")';
COMMENT ON COLUMN streets.street IS 'Street name without type (e.g., "Ленина")';