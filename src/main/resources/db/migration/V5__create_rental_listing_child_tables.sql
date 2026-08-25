-- ============================================================
-- V5__create_rental_listing_child_tables.sql
-- Rental listing child and relationship tables
-- ============================================================

CREATE TABLE rental_listing_images (
    image_id UUID PRIMARY KEY,

    listing_id UUID NOT NULL,
    image_url VARCHAR(1000) NOT NULL,
    display_order INTEGER NOT NULL DEFAULT 0,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_rental_listing_images_listings
        FOREIGN KEY (listing_id)
        REFERENCES rental_listings (listing_id)
);

CREATE INDEX IF NOT EXISTS idx_rental_listing_images_listing_id
    ON rental_listing_images (listing_id);


CREATE TABLE rental_listing_amenities (
    listing_id UUID NOT NULL,
    amenity_id UUID NOT NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT pk_rental_listing_amenities
        amenity_relation_id UUID PRIMARY KEY,

    CONSTRAINT fk_rental_listing_amenities_listings
        FOREIGN KEY (listing_id)
        REFERENCES rental_listings (listing_id),

    CONSTRAINT fk_rental_listing_amenities_amenities
        FOREIGN KEY (amenity_id)
        REFERENCES amenities (amenity_id)
);

CREATE INDEX IF NOT EXISTS idx_rental_listing_amenities_amenity_id
    ON rental_listing_amenities (amenity_id);


CREATE TABLE rental_listing_amenity_locations (
    amenity_location_id UUID PRIMARY KEY,

    listing_id UUID NOT NULL,

    type VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    distance NUMERIC(10, 2),

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_rental_listing_amenity_locations_listings
        FOREIGN KEY (listing_id)
        REFERENCES rental_listings (listing_id)
);

CREATE INDEX IF NOT EXISTS idx_rental_listing_amenity_locations_listing_id
    ON rental_listing_amenity_locations (listing_id);


CREATE TABLE rental_listing_cost_of_living (
    cost_of_living_id UUID PRIMARY KEY,

    listing_id UUID NOT NULL,

    electricity_bill NUMERIC(15, 2),
    water_bill NUMERIC(15, 2),
    services_fee NUMERIC(15, 2),
    deposit NUMERIC(15, 2),

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_rental_listing_cost_of_living_listing_id
        UNIQUE (listing_id),

    CONSTRAINT fk_rental_listing_cost_of_living_listings
        FOREIGN KEY (listing_id)
        REFERENCES rental_listings (listing_id)
);

CREATE INDEX IF NOT EXISTS idx_rental_listing_cost_of_living_listing_id
    ON rental_listing_cost_of_living (listing_id);