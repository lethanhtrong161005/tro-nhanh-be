package com.tronhanh.service;

import com.tronhanh.dto.request.auth.LoginRequest;
import com.tronhanh.dto.request.auth.LogoutRequest;
import com.tronhanh.dto.request.auth.RefreshTokenRequest;
import com.tronhanh.dto.request.auth.VerifyOtpRequest;
import com.tronhanh.dto.response.auth.LoginResponse;
import com.tronhanh.dto.response.auth.TokenResponse;
import com.tronhanh.dto.response.auth.UserProfileResponse;

/**
 * Service interface defining authentication, 2FA OTP verification, token refresh, and session logout operations.
 */
public interface AuthService
{

  /**
   * Handles Step 1 primary authentication via phone number and password.
   *
   * @param request LoginRequest containing phone number and password.
   * @return LoginResponse containing session ID for OTP verification.
   */
  LoginResponse login(LoginRequest request);

  /**
   * Handles Step 2 OTP verification and issues JWT Access and Refresh Tokens.
   *
   * @param request VerifyOtpRequest containing session ID and OTP code.
   * @return TokenResponse containing issued JWT tokens.
   */
  TokenResponse verifyOtp(VerifyOtpRequest request);

  /**
   * Issues new Access Token using active Refresh Token with Token Family Rotation.
   *
   * @param request RefreshTokenRequest containing Refresh Token.
   * @return TokenResponse containing newly issued tokens.
   */
  TokenResponse refreshToken(RefreshTokenRequest request);

  /**
   * Revokes Refresh Token family and adds Access Token JTI to Redis blacklist.
   *
   * @param authHeader Authorization header string containing Bearer Access Token.
   * @param logoutRequest LogoutRequest containing Refresh Token.
   */
  void logout(String authHeader, LogoutRequest logoutRequest);

  /**
   * Retrieves profile details of the currently authenticated user.
   *
   * @return UserProfileResponse containing user profile information.
   */
  UserProfileResponse getCurrentUserProfile();
}
