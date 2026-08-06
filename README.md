# 🏡 Tro Nhanh Backend (tro-nhanh-be)

Production-ready, monolithic enterprise backend for the **Tro Nhanh** platform, built with **Spring Boot 3.5.x**, **Java 21**, **PostgreSQL 16**, **Redis 7**, and **Flyway**.

---

## 🏗️ Architecture & Package Structure

The codebase strictly adheres to standard Spring Boot project layout under `com.tronhanh`:

```
src/main/java/com/tronhanh
│
├── TroNhanhBeApplication.java    # Global UTC timezone enforcement
│
├── config                        # AuditorAwareImpl, JpaConfig, MessageSourceConfig, OpenApiConfig, RedisConfig, SecurityConfig, WebConfig
├── constant                      # AppConstant, MessageCodeConstant
├── controller                    # AuthController, HealthController
├── dto
│   ├── request
│   │   ├── auth                  # LoginRequest, VerifyOtpRequest, RefreshTokenRequest, LogoutRequest
│   │   └── common                # BaseSortCondition, PageRequest, PayloadSearchRequest
│   ├── response
│   │   ├── auth                  # LoginResponse, TokenResponse, UserProfileResponse
│   │   └── common                # ApiResponse, HealthCheckResponse, LocalizedMessageDto, PageResponse
│   └── specification             # FilterCriteria, GenericSpecification, GenericSpecificationBuilder
├── entity                        # BaseEntity, RoleEntity, SystemRoleAssignmentEntity, UserEntity
├── enums                         # RoleName, SearchOperation, SortOrder, UserStatus
├── exception                     # HttpException, GlobalExceptionHandler
├── helper                        # UserHelper (Entity <-> DTO Mapping)
├── repository                    # RoleRepository, SystemRoleAssignmentRepository, UserRepository
├── security                      # JwtAuthenticationFilter, JwtProvider, SecurityConfig, TraceIdFilter
├── service                       # AuthService, RedisService
│   └── impl                      # AuthServiceImpl, RedisServiceImpl
├── util                          # CommonUtil, MessageUtils, ResponseUtils
└── validation                    # EnumValue, EnumValueValidator, I18nField, RequireField, RequireFieldValidator
```

---

## 🔐 Enterprise 2FA & Security Architecture

1. **Two-Factor Authentication (2FA Flow)**:
   - **Step 1 (`POST /api/v1/auth/login`)**: Validates phone number and password, generates a 6-digit OTP code (`CommonUtil.generateOtpCode()`) and a temporary `sessionId`, both cached in Redis with a 5-minute TTL.
   - **Step 2 (`POST /api/v1/auth/verify-otp`)**: Verifies OTP against Redis, invalidates temporary credentials, and issues Access Token & Refresh Token pair.
2. **Instant Access Token Blacklisting**:
   - Access Tokens contain a unique JWT ID (`jti`). Logout instantly blacklists `jti` in Redis (`blacklist:jti:<jti>`) with TTL matching the remaining token expiration time.
3. **Token Family Rotation & Theft Mitigation**:
   - Refresh Tokens belong to a `family_id`. Reusing an old/rotated Refresh Token triggers immediate revocation of the entire Token Family (`rt:revoked_family:<family_id>`).
4. **Detailed Specification**: Complete architecture documentation and Mermaid sequence diagrams are available in `.agents/bussiness/AUTHENTICATION_SPEC.md`.

---

## 🌐 Environment & Profiles

Configuration files use `.properties` format without fallback default values (`:` or `:-`):
- `src/main/resources/application.properties` — Base configuration (`${APP_PROFILE}`, `${SERVER_PORT}`)
- `src/main/resources/application-local.properties` — Local development profile (IntelliJ IDEA)
- `src/main/resources/application-dev.properties` — Development container profile (`dev`)
- `src/main/resources/application-prod.properties` — Production profile (`prod`)

> ⚠️ **Strict Property Enforcement**: All environment variables MUST be provided via `.env` files or system environment variables. Missing properties will trigger immediate application startup failure.

---

## 🚀 Running the Application

### 1️⃣ Local Development (IntelliJ IDEA + Docker Infrastructure)
Run ONLY PostgreSQL and Redis containers, while building/debugging Spring Boot directly inside IntelliJ IDEA:

```bash
# 1. Start local PostgreSQL & Redis infrastructure
docker compose -f docker-compose.local.yml up -d

# 2. Run / Debug TroNhanhBeApplication in IntelliJ IDEA using .env.local variables (Profile: local)

# 3. Stop infrastructure when finished
docker compose -f docker-compose.local.yml down
```

### 2️⃣ Development Container Mode
Full containerized stack including the backend application:

```bash
docker compose -f docker-compose.dev.yml up --build
```

### 3️⃣ Production Mode
```bash
docker compose -f docker-compose.prod.yml up --build -d
```

---

## ✉️ Message Code Constants & JavaDoc Standard

All message codes are centralized in `MessageCodeConstant` using self-referencing keys (`MSG_CODE_NNN = "MSG_CODE_NNN"`). Each constant includes JavaDoc documenting Vietnamese and English translations with `{0}` placeholders:

```java
  /**
   * <ul>
   *   <li>VI: {0} không tìm thấy.
   *   <li>EN: {0} not found.
   * </ul>
   */
  public static final String MSG_CODE_103 = "MSG_CODE_103";
```

---

## 🔍 Enterprise Specification Pattern

Dynamic database criteria queries are handled using `GenericSpecificationBuilder` and `SearchOperation`:

```java
Specification<UserEntity> spec = new GenericSpecificationBuilder<UserEntity>()
    .with("fullName", SearchOperation.LIKE, request.getFilter().getKeyword())
    .with("status", SearchOperation.EQUAL, request.getFilter().getStatus())
    .buildAnd();

Page<UserEntity> pageResult = userRepository.findAll(spec, pageable);
```

---

## 🏷️ Localized Field Validation (`@RequireField` + `@I18nField`)

Use `@RequireField` with `@I18nField` on DTO fields for localized validation:

```java
public class CreateUserRequest {

  @RequireField(
    messageCode = MessageCodeConstant.MSG_CODE_200,
    i18n = @I18nField(vi = "Họ và tên", en = "Full name")
  )
  private String fullName;
}
```

---

## 🛡️ Mandatory Coding Standards

1. **Allman Style Braces**: Open braces `{` placed on new lines for all classes, methods, and control blocks.
2. **Null Checks**: ALWAYS use `java.util.Objects.isNull(val)` or `Objects.nonNull(val)`. NEVER use `== null`.
3. **Empty String Checks**: ALWAYS use `str.isEmpty()` or `str.isBlank()`. NEVER use `str.equals("")`.
4. **Class Naming Suffixes**:
   - `constant`: `AppConstant`, `MessageCodeConstant`
   - `helper`: `UserHelper`
   - `util`: `CommonUtil`, `MessageUtils`, `ResponseUtils`

---

## 🔗 System Endpoints

- **Swagger UI**: `http://localhost:8089/swagger-ui/index.html`
- **Health Check**: `http://localhost:8089/api/v1/health`
- **OpenAPI Spec**: `http://localhost:8089/v3/api-docs`
