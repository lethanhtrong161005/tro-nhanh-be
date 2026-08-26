-- =============================================================================
-- V6: Create boost package master data and boost order tables
-- =============================================================================
-- Boost packages are admin-managed master data defining duration and price.
-- Boost orders track individual payment transactions for listing boosts.
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 1. boost_packages (master data)
-- -----------------------------------------------------------------------------
CREATE TABLE boost_packages (
    package_id     UUID PRIMARY KEY,
    code           VARCHAR(20)    NOT NULL,
    name           VARCHAR(100)   NOT NULL,
    description    TEXT,
    duration_days  INTEGER        NOT NULL,
    price          NUMERIC(15, 2) NOT NULL,
    is_active      BOOLEAN        NOT NULL DEFAULT TRUE,

    -- Mandatory audit & soft-delete columns (BaseEntity)
    created_at     TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMPTZ,
    created_by     VARCHAR(100),
    updated_by     VARCHAR(100),
    is_deleted     BOOLEAN        NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_boost_packages_code UNIQUE (code)
);

CREATE INDEX IF NOT EXISTS idx_boost_packages_is_active
    ON boost_packages (is_active);

-- -----------------------------------------------------------------------------
-- 2. boost_orders (payment tracking)
-- -----------------------------------------------------------------------------
CREATE TABLE boost_orders (
    order_id       UUID PRIMARY KEY,
    listing_id     UUID           NOT NULL,
    user_id        UUID           NOT NULL,
    package_id     UUID           NOT NULL,
    amount         NUMERIC(15, 2) NOT NULL,
    payment_method VARCHAR(30)    NOT NULL,
    status         VARCHAR(30)    NOT NULL DEFAULT 'PENDING',
    transaction_id VARCHAR(255),
    paid_at        TIMESTAMPTZ,
    expire_at      TIMESTAMPTZ,

    -- Mandatory audit & soft-delete columns (BaseEntity)
    created_at     TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMPTZ,
    created_by     VARCHAR(100),
    updated_by     VARCHAR(100),
    is_deleted     BOOLEAN        NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_boost_orders_listings FOREIGN KEY (listing_id)
        REFERENCES rental_listings (listing_id),
    CONSTRAINT fk_boost_orders_users FOREIGN KEY (user_id)
        REFERENCES users (user_id),
    CONSTRAINT fk_boost_orders_packages FOREIGN KEY (package_id)
        REFERENCES boost_packages (package_id)
);

CREATE INDEX IF NOT EXISTS idx_boost_orders_listing_id
    ON boost_orders (listing_id);
CREATE INDEX IF NOT EXISTS idx_boost_orders_user_id
    ON boost_orders (user_id);
CREATE INDEX IF NOT EXISTS idx_boost_orders_package_id
    ON boost_orders (package_id);
CREATE INDEX IF NOT EXISTS idx_boost_orders_status
    ON boost_orders (status);
CREATE INDEX IF NOT EXISTS idx_boost_orders_paid_at
    ON boost_orders (paid_at);

