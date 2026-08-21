-- ============================================================
-- V2__seed_admin_role_and_user.sql
-- Seed script for default Admin role and initial Admin user
-- ============================================================

-- 1. Insert ADMIN role
INSERT INTO roles (
    role_id,
    role_name,
    description,
    created_by
) VALUES (
             '018f0000-0000-7000-8000-000000000001',
             'ADMIN',
             'System Administrator with full permissions',
             'SYSTEM'
         );

-- 2. Insert initial Admin user (Password default: admin123)
INSERT INTO users (
    user_id,
    email,
    password_hash,
    first_name,
    last_name,
    full_name,
    phone_number,
    status,
    created_by
) VALUES (
             '018f0000-0000-7000-8000-000000000002',
             'admin@tronhanh.com',
             '$2a$12$3PkzHCu59pOJ6nUxV6E.Ou3ccoRsC00sj62lNlQt..qalY7Il5TUS', -- BCrypt hash cho "admin123"
             'System',
             'Admin',
             'System Administrator',
             '0900000000',
             'ACTIVE',
             'SYSTEM'
         );

-- 3. Assign ADMIN role to the Admin user
INSERT INTO system_role_assignments (
    user_id,
    role_id,
    created_by
) VALUES (
             '018f0000-0000-7000-8000-000000000002',
             '018f0000-0000-7000-8000-000000000001',
             'SYSTEM'
         );