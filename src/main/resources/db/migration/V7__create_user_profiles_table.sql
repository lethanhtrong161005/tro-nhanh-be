-- =============================================================================
-- V7: Create user_profiles table
-- =============================================================================
-- User profile table holding extended user information such as avatar URL,
-- date of birth, gender, bio, occupation, and address.
-- Maintains a strict 1-to-1 relationship with the users table.
-- =============================================================================

CREATE TABLE user_profiles (
    user_profile_id UUID           PRIMARY KEY,
    user_id         UUID           NOT NULL,
    avatar_url      VARCHAR(500),
    date_of_birth   DATE,
    gender          VARCHAR(20),
    bio             VARCHAR(500),
    occupation      VARCHAR(100),
    address         VARCHAR(255),
    city            VARCHAR(100),
    district        VARCHAR(100),
    ward            VARCHAR(100),

    -- Mandatory audit & soft-delete columns (BaseEntity)
    created_at      TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ,
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100),
    is_deleted      BOOLEAN        NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_user_profiles_user_id UNIQUE (user_id),
    CONSTRAINT fk_user_profiles_users FOREIGN KEY (user_id)
        REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_user_profiles_user_id
    ON user_profiles (user_id);
