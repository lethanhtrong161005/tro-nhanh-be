package com.tronhanh.service.impl;

import com.tronhanh.constant.AppConstant;
import com.tronhanh.constant.MessageCodeConstant;
import com.tronhanh.dto.request.auth.LoginRequest;
import com.tronhanh.dto.request.auth.LogoutRequest;
import com.tronhanh.dto.request.auth.RefreshTokenRequest;
import com.tronhanh.dto.request.auth.VerifyOtpRequest;
import com.tronhanh.dto.response.auth.LoginResponse;
import com.tronhanh.dto.response.auth.TokenResponse;
import com.tronhanh.dto.response.auth.UserProfileResponse;
import com.tronhanh.entity.UserEntity;
import com.tronhanh.enums.UserStatus;
import com.tronhanh.exception.HttpException;
import com.tronhanh.repository.UserRepository;
import com.tronhanh.security.JwtProvider;
import com.tronhanh.service.AuthService;
import com.tronhanh.service.RedisService;
import com.tronhanh.helper.UserHelper;
import com.tronhanh.util.CommonUtil;
import com.tronhanh.util.MessageUtils;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Enterprise Implementation of {@link AuthService} featuring 2FA, Token Family Rotation, and JTI Blacklist Logout.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService
{

  /**
   * User repository component.
   */
  private final UserRepository userRepository;

  /**
   * Password encoder component.
   */
  private final PasswordEncoder passwordEncoder;

  /**
   * JWT provider component.
   */
  private final JwtProvider jwtProvider;

  /**
   * Redis service component.
   */
  private final RedisService redisService;

  /**
   * User helper mapping component.
   */
  private final UserHelper userHelper;

  @Override
  @Transactional(readOnly = true)
  public LoginResponse login(LoginRequest request) {
    UserEntity user = userRepository.findByPhoneNumberAndIsDeletedFalse(request.getPhoneNumber())
        .orElseThrow(() -> new HttpException(
            HttpStatus.UNAUTHORIZED.value(),
            MessageUtils.getMessage(MessageCodeConstant.MSG_CODE_101)
        ));

    if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
      throw new HttpException(
          HttpStatus.UNAUTHORIZED.value(),
          MessageUtils.getMessage(MessageCodeConstant.MSG_CODE_101)
      );
    }

    if (user.getStatus() != UserStatus.ACTIVE) {
      throw new HttpException(
          HttpStatus.FORBIDDEN.value(),
          MessageUtils.getMessage(MessageCodeConstant.MSG_CODE_102)
      );
    }

    // Generate 6-digit OTP code & Session ID using CommonUtil
    String otpCode = CommonUtil.generateOtpCode();
    String sessionId = UUID.randomUUID().toString();

    // Save OTP & pending auth session in Redis for 5 minutes
    redisService.set(AppConstant.OTP_PREFIX + request.getPhoneNumber(), otpCode, AppConstant.OTP_TTL_MINUTES, TimeUnit.MINUTES);
    redisService.set(AppConstant.PENDING_AUTH_PREFIX + sessionId, request.getPhoneNumber(), AppConstant.OTP_TTL_MINUTES, TimeUnit.MINUTES);

    log.info("[2FA] Generated OTP code [{}] for phone number [{}] (Session: {})", otpCode, request.getPhoneNumber(), sessionId);

    String message = MessageUtils.getLocalizedText(MessageCodeConstant.MSG_CODE_001);
    return LoginResponse.builder()
        .sessionId(sessionId)
        .message(message)
        .expiresInSeconds(AppConstant.OTP_TTL_MINUTES * 60)
        .build();
  }

  @Override
  @Transactional
  public TokenResponse verifyOtp(VerifyOtpRequest request) {
    String phoneNumber = redisService.get(AppConstant.PENDING_AUTH_PREFIX + request.getSessionId());
    if (Objects.isNull(phoneNumber)) {
      throw new HttpException(
          HttpStatus.BAD_REQUEST.value(),
          MessageUtils.getMessage(MessageCodeConstant.MSG_CODE_201)
      );
    }

    String storedOtp = redisService.get(AppConstant.OTP_PREFIX + phoneNumber);
    if (Objects.isNull(storedOtp) || !storedOtp.equals(request.getOtpCode())) {
      throw new HttpException(
          HttpStatus.BAD_REQUEST.value(),
          MessageUtils.getMessage(MessageCodeConstant.MSG_CODE_201)
      );
    }

    // Clean up OTP and pending session from Redis
    redisService.delete(AppConstant.OTP_PREFIX + phoneNumber);
    redisService.delete(AppConstant.PENDING_AUTH_PREFIX + request.getSessionId());

    UserEntity user = userRepository.findByPhoneNumberAndIsDeletedFalse(phoneNumber)
        .orElseThrow(() -> new HttpException(
            HttpStatus.NOT_FOUND.value(),
            MessageUtils.getMessage(MessageCodeConstant.MSG_CODE_103, "User")
        ));

    // Generate new Token Family ID
    String familyId = UUID.randomUUID().toString();

    String accessToken = jwtProvider.generateAccessToken(user, familyId);
    String refreshToken = jwtProvider.generateRefreshToken(user, familyId);

    // Register active refresh token in Redis under the token family
    String jtiRt = jwtProvider.getJtiFromToken(refreshToken);
    redisService.set(
        AppConstant.RT_FAMILY_PREFIX + familyId + ":" + jtiRt,
        "ACTIVE",
        AppConstant.REFRESH_FAMILY_TTL_DAYS,
        TimeUnit.DAYS
    );

    return TokenResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }

  @Override
  @Transactional(readOnly = true)
  public TokenResponse refreshToken(RefreshTokenRequest request) {
    String refreshToken = request.getRefreshToken();
    if (!jwtProvider.validateToken(refreshToken)) {
      throw new HttpException(
          HttpStatus.UNAUTHORIZED.value(),
          MessageUtils.getMessage(MessageCodeConstant.MSG_CODE_201)
      );
    }

    UUID userId = jwtProvider.getUserIdFromToken(refreshToken);
    String familyId = jwtProvider.getFamilyIdFromToken(refreshToken);
    String jtiRt = jwtProvider.getJtiFromToken(refreshToken);

    // 1. Check if token family was revoked
    if (redisService.hasKey(AppConstant.RT_REVOKED_FAMILY_PREFIX + familyId)) {
      log.warn("[Token Theft] Attempt to use token from revoked family: {}", familyId);
      throw new HttpException(
          HttpStatus.UNAUTHORIZED.value(),
          MessageUtils.getMessage(MessageCodeConstant.MSG_CODE_201)
      );
    }

    // 2. Check if this specific refresh token JTI is active
    String familyKey = AppConstant.RT_FAMILY_PREFIX + familyId + ":" + jtiRt;
    if (!redisService.hasKey(familyKey)) {
      // Replay attack / Reuse of already rotated refresh token! Revoke entire family!
      log.error("[Token Theft Detected] Refresh token JTI {} already spent. Revoking family {}", jtiRt, familyId);
      redisService.set(AppConstant.RT_REVOKED_FAMILY_PREFIX + familyId, "REVOKED", AppConstant.REFRESH_FAMILY_TTL_DAYS, TimeUnit.DAYS);

      throw new HttpException(
          HttpStatus.UNAUTHORIZED.value(),
          MessageUtils.getMessage(MessageCodeConstant.MSG_CODE_201)
      );
    }

    // 3. Token is valid — invalidate spent JTI
    redisService.delete(familyKey);

    UserEntity user = userRepository.findById(userId)
        .orElseThrow(() -> new HttpException(
            HttpStatus.NOT_FOUND.value(),
            MessageUtils.getMessage(MessageCodeConstant.MSG_CODE_103, "User")
        ));

    // 4. Rotate tokens: Keep same familyId, generate new JTI for AT and RT
    String newAccessToken = jwtProvider.generateAccessToken(user, familyId);
    String newRefreshToken = jwtProvider.generateRefreshToken(user, familyId);

    String newJtiRt = jwtProvider.getJtiFromToken(newRefreshToken);
    redisService.set(
        AppConstant.RT_FAMILY_PREFIX + familyId + ":" + newJtiRt,
        "ACTIVE",
        AppConstant.REFRESH_FAMILY_TTL_DAYS,
        TimeUnit.DAYS
    );

    return TokenResponse.builder()
        .accessToken(newAccessToken)
        .refreshToken(newRefreshToken)
        .build();
  }

  @Override
  public void logout(String authHeader, LogoutRequest logoutRequest) {
    // 1. Blacklist Access Token via JTI
    if (Objects.nonNull(authHeader) && authHeader.startsWith("Bearer ")) {
      String accessToken = authHeader.substring(7);
      if (jwtProvider.validateToken(accessToken)) {
        String jtiAt = jwtProvider.getJtiFromToken(accessToken);
        long remainingMs = jwtProvider.getRemainingExpirationMs(accessToken);
        if (remainingMs > 0 && Objects.nonNull(jtiAt)) {
          redisService.set(AppConstant.BLACKLIST_JTI_PREFIX + jtiAt, "REVOKED", remainingMs, TimeUnit.MILLISECONDS);
          log.info("[Logout] Blacklisted Access Token JTI [{}] for {} ms", jtiAt, remainingMs);
        }
      }
    }

    // 2. Revoke Refresh Token & Token Family
    if (Objects.nonNull(logoutRequest) && Objects.nonNull(logoutRequest.getRefreshToken())) {
      String refreshToken = logoutRequest.getRefreshToken();
      if (jwtProvider.validateToken(refreshToken)) {
        String familyId = jwtProvider.getFamilyIdFromToken(refreshToken);
        String jtiRt = jwtProvider.getJtiFromToken(refreshToken);

        if (Objects.nonNull(familyId)) {
          redisService.set(AppConstant.RT_REVOKED_FAMILY_PREFIX + familyId, "REVOKED", AppConstant.REFRESH_FAMILY_TTL_DAYS, TimeUnit.DAYS);
          if (Objects.nonNull(jtiRt)) {
            redisService.delete(AppConstant.RT_FAMILY_PREFIX + familyId + ":" + jtiRt);
          }
          log.info("[Logout] Revoked Token Family [{}] and Refresh Token JTI [{}]", familyId, jtiRt);
        }
      }
    }
  }

  @Override
  @Transactional(readOnly = true)
  public UserProfileResponse getCurrentUserProfile() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (Objects.isNull(authentication) || !(authentication.getPrincipal() instanceof UserEntity user)) {
      throw new HttpException(
          HttpStatus.UNAUTHORIZED.value(),
          MessageUtils.getMessage(MessageCodeConstant.MSG_CODE_101)
      );
    }

    return userHelper.mapToUserProfileResponse(user);
  }
}
