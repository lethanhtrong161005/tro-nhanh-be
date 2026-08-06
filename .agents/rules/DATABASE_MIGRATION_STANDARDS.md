# 🗄️ Database Migration Standards (Flyway & PostgreSQL)

This document defines mandatory database migration rules and SQL coding conventions for the **Tro Nhanh** backend (`com.tronhanh`).

---

## 1. File Location & Naming Conventions

### 📁 Migration Directory
All migration scripts MUST be placed in:
`src/main/resources/db/migration/`

### 🏷️ Flyway File Naming Rules
Flyway requires a strict naming pattern using **double underscores (`__`)**:

```
V<VERSION>__<description>.sql
```

#### Examples:
- `V1__init_schema.sql` (Initial database schema setup)
- `V2__create_users_table.sql` (Creating users table)
- `V2.1__add_index_users_email.sql` (Minor index addition)
- `R__create_v_active_listings_view.sql` (Repeatable migration for views/functions)

> ⚠️ **CRITICAL RULE**: ALWAYS use a **double underscore (`__`)** between the version number and the description. Single underscores (`_`) will cause Flyway parsing errors!

---

## 2. Table & Column Naming Rules

| Object Type | Casing | Plurality | Example |
| :--- | :--- | :--- | :--- |
| **Table Name** | `snake_case` | **Plural** | `users`, `rental_listings`, `roles` |
| **Column Name** | `snake_case` | Singular | `created_at`, `user_id`, `is_deleted` |
| **Primary Key Constraint** | `pk_<table_name>` | Singular/Plural | `pk_users`, `pk_rental_listings` |
| **Foreign Key Constraint** | `fk_<table_name>_<target_table>` | Singular/Plural | `fk_rental_listings_users` |
| **Index Name** | `idx_<table_name>_<column_name>` | Singular/Plural | `idx_users_email`, `idx_listings_status` |

---

## 3. Mandatory Audit Columns Standard

Every new entity table MUST include the following base columns for JPA Auditing, Soft Delete, and Optimistic Locking compatibility:

```sql
CREATE TABLE users (
    UUID BIGSERIAL PRIMARY KEY,
    
    -- Domain specific columns
    email VARCHAR(255) NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    
    -- Mandatory Audit & Soft Delete Columns
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE

    CONSTRAINT uq_users_email UNIQUE (email)
);
```

---

## 4. SQL Writing Rules

1. **SQL Keywords**: Write ALL SQL keywords in **UPPERCASE** (`CREATE TABLE`, `ALTER TABLE`, `PRIMARY KEY`, `NOT NULL`, `DEFAULT`, `CONSTRAINT`, `FOREIGN KEY`, `INDEX`).
2. **PostgreSQL Data Types**:
   - Primary Keys: `BIGSERIAL` (or `UUID` if explicitly required by domain)
   - Timestamps: Always use `TIMESTAMPTZ` (UTC enforcement)
   - Text: `VARCHAR(n)` or `TEXT`
   - Numeric: `BIGINT`, `INTEGER`, `NUMERIC(15, 2)`
   - Booleans: `BOOLEAN` with `DEFAULT FALSE` or `DEFAULT TRUE`
3. **Idempotency & Safety**:
   - Always name Foreign Key and Unique Constraints explicitly (`CONSTRAINT fk_...`).
   - Create indexes concurrently or safely using `CREATE INDEX IF NOT EXISTS`.
   - NEVER drop columns or tables in production migration scripts without backward-compatible deprecation phases.
