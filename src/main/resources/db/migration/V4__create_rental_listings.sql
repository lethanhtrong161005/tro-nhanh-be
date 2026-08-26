-- ============================================================
-- V4__create_rental_listings.sql
-- Core rental listing table
-- ============================================================

CREATE TABLE rental_listings (
    listing_id UUID PRIMARY KEY,

    owner_id UUID NOT NULL,
    type_id UUID NOT NULL,

    title VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    address VARCHAR(500) NOT NULL,

    area NUMERIC(10, 2) NOT NULL,
    price NUMERIC(15, 2) NOT NULL,

    phone_contact VARCHAR(20),

    access_policy VARCHAR(20) NOT NULL DEFAULT 'FREE',
    access_open_time TIME,
    access_close_time TIME,

    description TEXT,

    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',

    is_boosted BOOLEAN NOT NULL DEFAULT FALSE,

    approved_at TIMESTAMPTZ,
    expire_at TIMESTAMPTZ,
    boost_expire_at TIMESTAMPTZ,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_rental_listings_users
        FOREIGN KEY (owner_id)
        REFERENCES users (user_id),

    CONSTRAINT fk_rental_listings_types
        FOREIGN KEY (type_id)
        REFERENCES rental_listing_types (type_id)
);

CREATE INDEX IF NOT EXISTS idx_rental_listings_owner_id
    ON rental_listings (owner_id);

CREATE INDEX IF NOT EXISTS idx_rental_listings_type_id
    ON rental_listings (type_id);

CREATE INDEX IF NOT EXISTS idx_rental_listings_status
    ON rental_listings (status);

CREATE INDEX IF NOT EXISTS idx_rental_listings_location
    ON rental_listings (location);

CREATE INDEX IF NOT EXISTS idx_rental_listings_price
    ON rental_listings (price);

CREATE INDEX IF NOT EXISTS idx_rental_listings_area
    ON rental_listings (area);

CREATE INDEX IF NOT EXISTS idx_rental_listings_boost_expire_at
    ON rental_listings (boost_expire_at);