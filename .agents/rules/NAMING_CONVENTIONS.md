# 🏷️ Naming Conventions (MANDATORY)

**Project**: Tro Nhanh BE | **Base package**: `com.tronhanh`

---

## 1. General Identifiers

| Element | Convention | Example |
|---------|-----------|---------|
| Class / Interface | `PascalCase` | `ListingService`, `UserRepository` |
| Method / Variable | `camelCase` | `findByEmail()`, `listingId` |
| Constant | `UPPER_SNAKE_CASE` | `MAX_RETRY_COUNT` |
| Package | lowercase, dot-separated | `com.tronhanh.service` |
| REST endpoint | kebab-case, plural nouns | `/api/v1/rental-listings` |
| DB table | `snake_case`, plural | `rental_listings` |
| DB column | `snake_case` | `created_at`, `is_deleted` |
| DTO suffix | `Request` / `Response` | `CreateListingRequest`, `ListingResponse` |
- **Controllers**: Suffix with `Controller` (e.g., `UserController`, `HealthController`).
  - MUST annotate class with `@Tag(name = "...", description = "...")`.
  - MUST annotate endpoint methods with `@Operation(summary = "...")`.
- **Request/Response DTOs**: Suffix with `Request` or `Response` (e.g., `CreateUserRequest`, `HealthCheckResponse`).
  - MUST annotate class with `@Schema(description = "...")`.
  - MUST annotate fields with `@Schema(description = "...", example = "...")`. |
| Entity | singular noun, no suffix | `Listing`, `User` |
| Exception | suffix `Exception` | `ListingNotFoundException` |
| Test class | suffix `Test` | `ListingServiceTest` |
| Boolean field/method | prefix `is` / `has` / `can` | `isDeleted`, `hasPermission()` |

---

## 2. Package Suffix Rules (MANDATORY)

| Package | Required suffix | ✅ Example | ❌ Wrong |
|---------|----------------|-----------|---------|
| `constant` | `Constant` | `AppConstant`, `MessageCodeConstant` | `MessageCode`, `Codes` |
| `config` | `Config` | `JpaConfig`, `WebConfig` | `JpaConfiguration` |
| `security` | `Config` or `Filter` | `SecurityConfig`, `TraceIdFilter` | `Security` |
| `util` | `Utils` or `Helper` | `MessageUtils`, `ResponseHelper` | `MessageUtil` |
| `mapper` | `Mapper` | `UserMapper` | `UserConvert` |
| `validation` | `Validator` (impl) | `RequireFieldValidator` | `RequireCheck` |
| `enums` | *(none — noun)* | `Role`, `ListingStatus` | `RoleEnum` |

---

## 3. Message Constant Naming

### Class: `MessageCodeConstant` in `constant` package

All message codes in one central class. Self-referencing value = key name. Each message constant MUST have a JavaDoc documenting its Vietnamese (VI) and English (EN) translations using `<ul><li>VI: ... <li>EN: ... </ul>`:

```java
  /**
   * <ul>
   *   <li>VI: Thao tác thành công.
   *   <li>EN: Operation successful.
   * </ul>
   */
  public static final String MSG_CODE_001 = "MSG_CODE_001";
```

### Reusable `{0}`-parameterized templates

Reuse before adding new codes:

| Code | Template (EN) | Template (VI) |
|------|--------------|--------------|
| `MSG_CODE_002` | `{0} created successfully.` | `{0} tạo thành công.` |
| `MSG_CODE_003` | `{0} updated successfully.` | `{0} cập nhật thành công.` |
| `MSG_CODE_004` | `{0} deleted successfully.` | `{0} xóa thành công.` |
| `MSG_CODE_103` | `{0} not found.` | `{0} không tìm thấy.` |
| `MSG_CODE_104` | `{0} already exists.` | `{0} đã tồn tại.` |
| `MSG_CODE_200` | `{0} is required.` | `{0} là bắt buộc.` |

Usage:
```java
ResponseUtils.successWithData(data, MessageCodeConstant.MSG_CODE_002, "Listing")
// → "Listing created successfully."
```

### i18n Field Validation — `@RequireField` + `@I18nField`

```java
@RequireField(i18n = @I18nField(vi = "Họ và tên", en = "Full name"))
private String fullName;
// EN: "Full name is required."
// VI: "Họ và tên là bắt buộc."
```

---

## 4. Package Structure (Flat Monolithic)

```
src/main/java/com/tronhanh
│
├── TroNhanhBeApplication.java
│
├── config               # JpaConfig, WebConfig, OpenApiConfig, AuditorAwareImpl
├── constant             # AppConstant, MessageCodeConstant
├── controller           # *Controller
├── dto
│   ├── request          # *Request (PageRequest, PayloadSearchRequest, ...)
│   └── response         # *Response, ApiResponse, LocalizedMessageDto, PageResponse
├── entity               # BaseEntity, * (extends BaseEntity)
├── enums                # Role, *Status, *Type
├── exception            # HttpException, GlobalExceptionHandler
├── mapper               # *Mapper (MapStruct interfaces)
├── repository           # *Repository
├── security             # SecurityConfig, TraceIdFilter, JWT helpers
├── service              # *Service (interfaces)
│   └── impl             # *ServiceImpl
├── util                 # MessageUtils, ResponseHelper
└── validation           # I18nField, RequireField, RequireFieldValidator
```

---

## 5. General Rules

- English only in code, comments, identifiers.
- No abbreviations except: `id`, `url`, `dto`, `jwt`, `api`.
- No generic names (`data`, `info`) without a qualifying prefix.
- No Vietnamese or non-ASCII in identifiers.
- Service interface in `service/`, implementation in `service/impl/`.
- Config classes ONLY in `config/` or `security/` — never inside `controller`, `service`, etc.
