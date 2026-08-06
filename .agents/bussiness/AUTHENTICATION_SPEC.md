# Enterprise 2FA & Token Management Architecture Specification

## 1. Overview & Security Architecture

Tro Nhanh Backend implements an **Enterprise-Grade Two-Factor Authentication (2FA)** and **Token Management System** built on top of Spring Security 6, JWT, and Redis.

### Key Security Design Patterns

1. **Two-Factor Authentication (2FA)**:
   - **Step 1 (Credential Validation)**: Phone number + Password validation generates a 6-digit OTP code and a temporary session ID stored in Redis (TTL: 5 minutes).
   - **Step 2 (OTP Verification)**: Validates OTP against Redis, invalidates temporary credentials, and issues JWT Access & Refresh Token pair bound to a unique Token Family.
2. **JTI-based Instant Access Token Blacklisting**:
   - Access tokens contain a unique JWT ID (`jti`). Upon logout, the `jti` is stored in Redis (`blacklist:jti:<jti>`) with a TTL matching the token's remaining lifespan, ensuring instant stateless revocation.
3. **Token Family Rotation (Mitigating Refresh Token Theft)**:
   - Every Refresh Token belongs to a `family_id`.
   - When a Refresh Token is used, it is rotated (a new Refresh Token JTI is issued under the same `family_id`).
   - If an old/spent Refresh Token JTI is presented (Replay Attack), the system detects token theft and immediately revokes the **entire Token Family** (`rt:revoked_family:<family_id>`).
4. **Role-Based Access Control (RBAC)**:
   - Enforcing strict roles (`RoleName` Enum: `USER`, `ADMIN`).
   - Dynamic profile mapping via `UserHelper`.

---

## 2. Redis Key Structure & TTL Specifications

| Key Pattern | Data Type | Purpose | TTL |
| :--- | :--- | :--- | :--- |
| `otp:<phoneNumber>` | String | Stores 6-digit OTP verification code | 5 minutes (`OTP_TTL_MINUTES`) |
| `pending_auth:<sessionId>` | String | Maps temporary 2FA session to phone number | 5 minutes (`OTP_TTL_MINUTES`) |
| `rt:family:<familyId>:<jtiRt>` | String | Identifies active Refresh Token JTI within family | 7 days (`REFRESH_FAMILY_TTL_DAYS`) |
| `rt:revoked_family:<familyId>` | String | Marks entire Token Family as revoked upon theft/logout | 7 days (`REFRESH_FAMILY_TTL_DAYS`) |
| `blacklist:jti:<jtiAt>` | String | Blacklists Access Token JTI upon logout | Remaining AT lifespan (ms) |

---

## 3. Sequence Diagrams

### 3.1 Flow 1: Step 1 Login (`POST /api/v1/auth/login`)

```mermaid
sequenceDiagram
    autonumber
    actor Client
    participant Controller as AuthController
    participant Service as AuthServiceImpl
    participant Util as CommonUtil
    participant Repo as UserRepository
    participant DB as PostgreSQL
    participant Redis as Redis Cache

    Client->>Controller: POST /api/v1/auth/login (phone, password)
    Controller->>Service: login(LoginRequest)
    Service->>Repo: findByPhoneNumberAndIsDeletedFalse(phone)
    Repo->>DB: SELECT * FROM users WHERE phone_number = ?
    DB-->>Repo: UserEntity
    Repo-->>Service: UserEntity

    alt Credentials Invalid or Status INACTIVE
        Service-->>Client: 401 Unauthorized / 403 Forbidden (HttpException)
    end

    Service->>Util: generateOtpCode()
    Util-->>Service: "123456"
    Service->>Service: Generate random sessionId (UUID)

    Service->>Redis: set("otp:<phone>", "123456", 5m)
    Service->>Redis: set("pending_auth:<sessionId>", "<phone>", 5m)

    Service-->>Controller: LoginResponse (sessionId, expiresInSeconds: 300)
    Controller-->>Client: 200 OK Response (ApiResponse<LoginResponse>)
```

---

### 3.2 Flow 2: Step 2 OTP Verification (`POST /api/v1/auth/verify-otp`)

```mermaid
sequenceDiagram
    autonumber
    actor Client
    participant Controller as AuthController
    participant Service as AuthServiceImpl
    participant Redis as Redis Cache
    participant Jwt as JwtProvider
    participant DB as PostgreSQL

    Client->>Controller: POST /api/v1/auth/verify-otp (sessionId, otpCode)
    Controller->>Service: verifyOtp(VerifyOtpRequest)

    Service->>Redis: get("pending_auth:<sessionId>")
    Redis-->>Service: phoneNumber

    alt Session expired or invalid
        Service-->>Client: 400 Bad Request (MSG_CODE_201)
    end

    Service->>Redis: get("otp:<phoneNumber>")
    Redis-->>Service: storedOtp

    alt OTP mismatch or expired
        Service-->>Client: 400 Bad Request (MSG_CODE_201)
    end

    Service->>Redis: delete("otp:<phoneNumber>")
    Service->>Redis: delete("pending_auth:<sessionId>")

    Service->>Service: Generate new familyId (UUID)
    Service->>Jwt: generateAccessToken(user, familyId)
    Jwt-->>Service: accessToken (with JTI, role, family_id)
    Service->>Jwt: generateRefreshToken(user, familyId)
    Jwt-->>Service: refreshToken (with JTI, family_id)

    Service->>Jwt: getJtiFromToken(refreshToken)
    Jwt-->>Service: jtiRt

    Service->>Redis: set("rt:family:<familyId>:<jtiRt>", "ACTIVE", 7 days)

    Service-->>Controller: TokenResponse (accessToken, refreshToken)
    Controller-->>Client: 200 OK Response (ApiResponse<TokenResponse>)
```

---

### 3.3 Flow 3: Token Rotation (`POST /api/v1/auth/refresh-token`)

```mermaid
sequenceDiagram
    autonumber
    actor Client
    participant Controller as AuthController
    participant Service as AuthServiceImpl
    participant Jwt as JwtProvider
    participant Redis as Redis Cache

    Client->>Controller: POST /api/v1/auth/refresh-token (refreshToken)
    Controller->>Service: refreshToken(RefreshTokenRequest)

    Service->>Jwt: validateToken(refreshToken)
    alt Invalid Signature or Expired
        Service-->>Client: 401 Unauthorized
    end

    Service->>Jwt: Extract userId, familyId, jtiRt
    
    Service->>Redis: hasKey("rt:revoked_family:<familyId>")
    alt Family Revoked
        Service-->>Client: 401 Unauthorized
    end

    Service->>Redis: hasKey("rt:family:<familyId>:<jtiRt>")
    alt JTI not found (Token already spent / Theft Attempt)
        Service->>Redis: set("rt:revoked_family:<familyId>", "REVOKED", 7 days)
        Service-->>Client: 401 Unauthorized (Token Theft Detected)
    end

    Service->>Redis: delete("rt:family:<familyId>:<jtiRt>")

    Service->>Jwt: generateAccessToken(user, familyId)
    Service->>Jwt: generateRefreshToken(user, familyId)

    Service->>Jwt: getJtiFromToken(newRefreshToken)
    Jwt-->>Service: newJtiRt

    Service->>Redis: set("rt:family:<familyId>:<newJtiRt>", "ACTIVE", 7 days)

    Service-->>Controller: TokenResponse (newAccessToken, newRefreshToken)
    Controller-->>Client: 200 OK Response (ApiResponse<TokenResponse>)
```

---

### 3.4 Flow 4: Enterprise Logout (`POST /api/v1/auth/logout`)

```mermaid
sequenceDiagram
    autonumber
    actor Client
    participant Controller as AuthController
    participant Service as AuthServiceImpl
    participant Jwt as JwtProvider
    participant Redis as Redis Cache

    Client->>Controller: POST /api/v1/auth/logout (Authorization: Bearer AT, Body: refreshToken)
    Controller->>Service: logout(authHeader, LogoutRequest)

    opt Blacklist Access Token
        Service->>Jwt: validateToken(accessToken)
        Service->>Jwt: getJtiFromToken(accessToken)
        Jwt-->>Service: jtiAt
        Service->>Jwt: getRemainingExpirationMs(accessToken)
        Jwt-->>Service: remainingMs
        Service->>Redis: set("blacklist:jti:<jtiAt>", "REVOKED", remainingMs)
    end

    opt Revoke Refresh Token & Family
        Service->>Jwt: validateToken(refreshToken)
        Service->>Jwt: getFamilyIdFromToken(refreshToken)
        Service->>Jwt: getJtiFromToken(refreshToken)
        Service->>Redis: set("rt:revoked_family:<familyId>", "REVOKED", 7 days)
        Service->>Redis: delete("rt:family:<familyId>:<jtiRt>")
    end

    Service-->>Controller: Void
    Controller-->>Client: 200 OK Response (ApiResponse<Void>)
```

---

### 3.5 Flow 5: Security Filter Inspection (`JwtAuthenticationFilter`)

```mermaid
sequenceDiagram
    autonumber
    actor Client
    participant Filter as JwtAuthenticationFilter
    participant Jwt as JwtProvider
    participant Redis as Redis Cache
    participant Context as SecurityContextHolder

    Client->>Filter: Request with Authorization: Bearer <token>
    Filter->>Jwt: validateToken(token)

    alt Token Invalid / Expired
        Filter->>Filter: Pass to filterChain (Unauthenticated)
    else Token Valid
        Filter->>Jwt: getJtiFromToken(token)
        Jwt-->>Filter: jtiAt
        Filter->>Redis: hasKey("blacklist:jti:<jtiAt>")
        alt JTI Blacklisted
            Filter-->>Client: 401 Unauthorized (JSON ApiResponse)
        else JTI Clean
            Filter->>Jwt: getUserIdFromToken(token)
            Filter->>Context: setAuthentication(UsernamePasswordAuthenticationToken)
            Filter->>Filter: filterChain.doFilter(request, response)
        end
    end
```

---

## 4. Components & Package Layout

```
com.tronhanh
├── config
│   └── SecurityConfig.java         # Public endpoint matchers & Filter Chain setup
├── constant
│   └── AppConstant.java            # PUBLIC_ENDPOINTS, Redis prefixes & TTLs
├── controller
│   └── AuthController.java         # Authentication REST endpoints
├── dto
│   ├── request
│   │   └── auth                    # LoginRequest, VerifyOtpRequest, RefreshTokenRequest, LogoutRequest
│   └── response
│       └── auth                    # LoginResponse, TokenResponse, UserProfileResponse
├── entity
│   ├── RoleEntity.java             # System roles with RoleName enum
│   └── UserEntity.java             # User entity
├── enums
│   └── RoleName.java               # USER, ADMIN enum
├── helper
│   └── UserHelper.java             # Entity to DTO mapping helper
├── security
│   ├── JwtAuthenticationFilter.java# Blacklist checking filter
│   └── JwtProvider.java            # Token generation & verification
└── service
    ├── AuthService.java            # Service contract
    └── impl
        └── AuthServiceImpl.java    # 2FA logic, Redis token rotation & revocation
```
