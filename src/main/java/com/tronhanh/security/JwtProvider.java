package com.tronhanh.security;

import com.tronhanh.entity.UserEntity;
import com.tronhanh.util.CommonUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * JWT Provider component for generating, parsing, and validating Access Tokens and Refresh Tokens.
 *
 * <p>Supports JTI (JWT ID) claims for token tracking and Token Family ID claims for token rotation.
 */
@Slf4j
@Component
public class JwtProvider {

  /** Secret key for signing JWT tokens. */
  @Value("${tn.jwt.secret}")
  private String jwtSecret;

  /** Access token expiration duration in milliseconds. */
  @Value("${tn.jwt.access-token-expiration-ms}")
  private long accessTokenExpirationMs;

  /** Refresh token expiration duration in milliseconds. */
  @Value("${tn.jwt.refresh-token-expiration-ms}")
  private long refreshTokenExpirationMs;

  /**
   * Generates signing key for HMAC-SHA algorithm.
   *
   * @return SecretKey instance.
   */
  private SecretKey getSigningKey() {
    byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  /**
   * Generates Access Token containing JTI, family ID, user ID, email, phone number, and role
   * claims.
   *
   * @param user UserEntity instance.
   * @param familyId Token family UUID string.
   * @return Signed JWT Access Token string.
   */
  public String generateAccessToken(UserEntity user, String familyId) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + accessTokenExpirationMs);
    String jti = CommonUtil.generateUuidV7().toString();

    String roleName =
        (Objects.nonNull(user.getSystemRoleAssignment())
                && Objects.nonNull(user.getSystemRoleAssignment().getRole())
                && Objects.nonNull(user.getSystemRoleAssignment().getRole().getRoleName()))
            ? user.getSystemRoleAssignment().getRole().getRoleName().name()
            : null;

    return Jwts.builder()
        .id(jti)
        .subject(user.getUserId().toString())
        .claim("family_id", familyId)
        .claim("email", Objects.nonNull(user.getEmail()) ? user.getEmail() : "")
        .claim("phoneNumber", Objects.nonNull(user.getPhoneNumber()) ? user.getPhoneNumber() : "")
        .claim("fullName", Objects.nonNull(user.getFullName()) ? user.getFullName() : "")
        .claim("role", roleName)
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(getSigningKey())
        .compact();
  }

  /**
   * Generates Refresh Token containing JTI, family ID, and user ID.
   *
   * @param user UserEntity instance.
   * @param familyId Token family UUID string.
   * @return Signed JWT Refresh Token string.
   */
  public String generateRefreshToken(UserEntity user, String familyId) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + refreshTokenExpirationMs);
    String jti = CommonUtil.generateUuidV7().toString();

    return Jwts.builder()
        .id(jti)
        .subject(user.getUserId().toString())
        .claim("family_id", familyId)
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(getSigningKey())
        .compact();
  }

  /**
   * Parses signed claims payload from token string.
   *
   * @param token JWT token string.
   * @return Claims payload object.
   */
  public Claims getClaimsFromToken(String token) {
    return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
  }

  /**
   * Extracts User ID (subject) from token string.
   *
   * @param token JWT token string.
   * @return User UUID instance.
   */
  public UUID getUserIdFromToken(String token) {
    Claims claims = getClaimsFromToken(token);
    return UUID.fromString(claims.getSubject());
  }

  /**
   * Extracts JTI (token ID) from token string.
   *
   * @param token JWT token string.
   * @return JTI string.
   */
  public String getJtiFromToken(String token) {
    Claims claims = getClaimsFromToken(token);
    return claims.getId();
  }

  /**
   * Extracts family ID claim from token string.
   *
   * @param token JWT token string.
   * @return Family ID string.
   */
  public String getFamilyIdFromToken(String token) {
    Claims claims = getClaimsFromToken(token);
    return claims.get("family_id", String.class);
  }

  /**
   * Calculates remaining time in milliseconds before token expiration.
   *
   * @param token JWT token string.
   * @return Remaining milliseconds or 0 if expired/invalid.
   */
  public long getRemainingExpirationMs(String token) {
    try {
      Claims claims = getClaimsFromToken(token);
      long expirationTime = claims.getExpiration().getTime();
      long currentTime = System.currentTimeMillis();
      return Math.max(0, expirationTime - currentTime);
    } catch (Exception e) {
      return 0;
    }
  }

  /**
   * Validates JWT token signature and expiration date.
   *
   * @param token JWT token string to validate.
   * @return True if token signature is valid and not expired.
   */
  public boolean validateToken(String token) {
    try {
      Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      log.error("Invalid JWT token: {}", e.getMessage());
    }
    return false;
  }

  /**
   * Gets configured access token expiration time.
   *
   * @return Access token expiration in milliseconds.
   */
  public long getAccessTokenExpirationMs() {
    return accessTokenExpirationMs;
  }

  /**
   * Gets configured refresh token expiration time.
   *
   * @return Refresh token expiration in milliseconds.
   */
  public long getRefreshTokenExpirationMs() {
    return refreshTokenExpirationMs;
  }
}
