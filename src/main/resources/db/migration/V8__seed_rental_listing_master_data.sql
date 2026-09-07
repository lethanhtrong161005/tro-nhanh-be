-- ============================================================
-- V7__seed_rental_listing_master_data.sql
-- Seed script for default Rental Listing Types and Amenities
-- ============================================================

-- 1. Insert default rental listing types
INSERT INTO rental_listing_types (
    type_id,
    code,
    name,
    description,
    created_by
) VALUES
    ('8ca4b1b3-441d-45cb-a89e-0498db6d9d1e', 'PHONG_TRO', 'Phòng trọ / Nhà trọ', 'Phòng cho thuê trong dãy trọ hoặc nhà riêng lẻ chia phòng, phù hợp cho học sinh, sinh viên và người lao động.', 'SYSTEM'),
    ('6f932e62-c84d-450e-bc21-085e683cf33c', 'NHA_NGUYEN_CAN', 'Nhà nguyên căn', 'Thuê trọn gói toàn bộ một căn nhà riêng lẻ, thích hợp cho hộ gia đình hoặc nhóm bạn ở đông người.', 'SYSTEM'),
    ('acbc62c0-2f95-442a-a9e9-74d6c41b83cc', 'CAN_HO', 'Căn hộ chung cư', 'Căn hộ thuộc dự án chung cư thương mại hoặc chung cư mini, đầy đủ tiện ích và an ninh.', 'SYSTEM'),
    ('ebd20de9-b54c-4ecf-a0bb-26cb4db179aa', 'O_GHEP', 'Ở ghép (Shared Room)', 'Tìm người ở ghép để chia sẻ không gian phòng và tối ưu chi phí thuê phòng.', 'SYSTEM'),
    ('fa249a15-055a-493e-8c8f-2878ff6c406b', 'CAN_HO_DICH_VU', 'Căn hộ dịch vụ', 'Căn hộ cao cấp có kèm theo các dịch vụ dọn dẹp, giặt ủi, internet và tiện ích trọn gói.', 'SYSTEM');

-- 2. Insert default predefined amenities
INSERT INTO amenities (
    amenity_id,
    code,
    name,
    icon,
    created_by
) VALUES
    ('df9d5f76-bc39-4d6f-aa7a-8b1e069695d1', 'WIFI', 'Wi-Fi / Internet', 'wifi', 'SYSTEM'),
    ('37f26197-047f-44e4-a1a7-cbf653063bc9', 'PARKING', 'Chỗ để xe (Parking)', 'parking', 'SYSTEM'),
    ('95fe71a2-4a0b-4024-811c-6b3a0fe18ff4', 'AIR_CONDITIONER', 'Điều hòa (Air Conditioner)', 'ac_unit', 'SYSTEM'),
    ('8efbc006-dc20-4ee8-b570-5bbf89849c33', 'ELEVATOR', 'Thang máy (Elevator)', 'elevator', 'SYSTEM'),
    ('42ec3cb9-25ea-4eb8-b99b-d7488df67bbd', 'SWIMMING_POOL', 'Bể bơi (Swimming Pool)', 'pool', 'SYSTEM'),
    ('b8c56fa7-cda4-43cb-b0b3-f0fa80dfca6e', 'WASHING_MACHINE', 'Máy giặt (Washing Machine)', 'local_laundry_service', 'SYSTEM'),
    ('d29fc6e2-2244-4841-ba63-df6a49db71c7', 'REFRIGERATOR', 'Tủ lạnh (Refrigerator)', 'kitchen', 'SYSTEM'),
    ('4ab247fe-9e0c-40b9-87c2-9e99eb0f3b0f', 'PRIVATE_BATHROOM', 'Vệ sinh khép kín (Private Bathroom)', 'bathtub', 'SYSTEM'),
    ('d3f8a0a4-fa0a-4fb4-9cbe-13bfbb214fbc', 'BALCONY', 'Ban công (Balcony)', 'balcony', 'SYSTEM'),
    ('ea511c77-6bc8-43d8-a14d-17db23e0a02f', 'SECURITY_247', 'An ninh 24/7 / Camera', 'security', 'SYSTEM');

