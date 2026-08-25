-- ============================================================
-- V3__create_rental_listing_master_data.sql
-- Rental Listing master data
-- ============================================================

CREATE TABLE rental_listing_types (
    type_id UUID PRIMARY KEY,
    code VARCHAR(20) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_rental_listing_types_code UNIQUE (code)
);

CREATE TABLE amenities (
    amenity_id UUID PRIMARY KEY,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    icon VARCHAR(255),

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_amenities_code UNIQUE (code)
);

CREATE INDEX IF NOT EXISTS idx_rental_listing_types_name
    ON rental_listing_types (name);

CREATE INDEX IF NOT EXISTS idx_amenities_name
    ON amenities (name);