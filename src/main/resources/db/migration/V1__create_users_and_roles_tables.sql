-- ============================================================
-- V1__create_users_and_roles_tables.sql
-- Flyway Database Migration script for Tro Nhanh Backend
-- ============================================================

-- 1. Create roles table
CREATE TABLE roles (
    role_id UUID PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL,
    description_vi TEXT,
    description_en TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT uq_roles_role_name UNIQUE (role_name)
);

-- 2. Create users table
CREATE TABLE users (
    user_id UUID PRIMARY KEY,
    email VARCHAR(100) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    full_name VARCHAR(120),
    phone_number VARCHAR(20),
    status VARCHAR(20) NOT NULL DEFAULT 'INACTIVE',
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT uq_users_email UNIQUE (email)
);

-- 3. Create system_role_assignments table
CREATE TABLE system_role_assignments (
    system_role_assign_id BIGSERIAL PRIMARY KEY,
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT uq_system_role_assignments_user_id UNIQUE (user_id),
    CONSTRAINT fk_system_role_assignments_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    CONSTRAINT fk_system_role_assignments_role FOREIGN KEY (role_id) REFERENCES roles (role_id) ON DELETE CASCADE
);

-- Indexes for performance optimization
CREATE INDEX idx_users_email ON users (email);
CREATE INDEX idx_system_role_assignments_user ON system_role_assignments (user_id);
CREATE INDEX idx_system_role_assignments_role ON system_role_assignments (role_id);
