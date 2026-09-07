-- ============================================================
-- V7__add_rental_listing_search_indexes.sql
-- Indexes for paginated rental listing search and filtering
-- ============================================================

CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE INDEX IF NOT EXISTS idx_rental_listings_title_trgm
    ON rental_listings USING GIN (LOWER(title) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS idx_rental_listings_description_trgm
    ON rental_listings USING GIN (LOWER(description) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS idx_rental_listings_location_trgm
    ON rental_listings USING GIN (LOWER(location) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS idx_rental_listings_deleted_status_created
    ON rental_listings (is_deleted, status, created_at DESC, listing_id);

CREATE INDEX IF NOT EXISTS idx_rental_listings_deleted_type_price
    ON rental_listings (is_deleted, type_id, price);
