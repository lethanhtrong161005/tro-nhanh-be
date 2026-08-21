package com.tronhanh.service;

import com.tronhanh.dto.request.auth.LoginRequest;
import com.tronhanh.dto.request.auth.ResendOtpRequest;
import com.tronhanh.dto.request.auth.VerifyOtpRequest;
import com.tronhanh.dto.response.auth.LoginResponse;
import jakarta.servlet.http.HttpServletResponse;
import com.tronhanh.dto.response.auth.UserProfileResponse;
import com.tronhanh.exception.HttpException;

/**
 * Service interface defining authentication, 2FA OTP verification, token refresh, and session
 * logout operations.
 */
public interface AuthService {

  /**
   * Authenticates a user with phone number and password, generating a 2FA OTP session upon success.
   *
   * <p>Step-by-step flow:
   *
   * <ol>
   *   <li>Find active user entity by phone number from repository
   *   <li>Verify raw password against hashed password
   *   <li>Check if user status is ACTIVE
   *   <li>Generate 6-digit OTP code and unique session ID
   *   <li>Store OTP and pending auth session mapping in Redis with expiration
   *   <li>Return session ID and expiration time in response
   * </ol>
   *
   * @param request The login credentials containing phone number and raw password. Must not be
   *     null.
   * @return {@link LoginResponse} Containing the session ID and OTP validity duration in seconds.
   * @throws HttpException If user is not found or password mismatched (401 UNAUTHORIZED).
   * @throws HttpException If user account status is not ACTIVE (403 FORBIDDEN).
   */
  LoginResponse login(LoginRequest request);

  /**
   * Verifies the 2FA OTP code using the provided session ID and issues JWT tokens upon success.
   *
   * <p>Step-by-step flow:
   *
   * <ol>
   *   <li>Retrieve pending phone number by session ID from Redis.
   *   <li>Validate the provided OTP code against the stored OTP.
   *   <li>Remove used OTP and session from Redis to prevent replay attacks.
   *   <li>Retrieve the active user entity from the database.
   *   <li>Generate new access and refresh tokens with a token family ID.
   *   <li>Register the refresh token in Redis for token rotation tracking.
   * </ol>
   *
   * @param request The verification request containing session ID and OTP code. Must not be null.
   * @param response HttpServletResponse to set cookies.
   * @throws HttpException If session expires or OTP is invalid (400 BAD_REQUEST).
   * @throws HttpException If the user account is not found (404 NOT_FOUND).
   */
  void verifyOtp(VerifyOtpRequest request, HttpServletResponse response);

  /**
   * Issues new Access Token using active Refresh Token with Token Family Rotation.
   *
   * @param refreshToken The Refresh Token string.
   * @param response HttpServletResponse to set cookies.
   */
  void refreshToken(String refreshToken, HttpServletResponse response);

  /**
   * Revokes Refresh Token family and adds Access Token JTI to Redis blacklist.
   *
   * @param accessToken The Access Token string (extracted from Cookie or Header).
   * @param refreshToken The Refresh Token string to revoke.
   * @param response HttpServletResponse to clear cookies.
   */
  void logout(String accessToken, String refreshToken, HttpServletResponse response);

  /**
   * Retrieves profile details of the currently authenticated user.
   *
   * @return UserProfileResponse containing user profile information.
   */
  UserProfileResponse getCurrentUserProfile();

  /**
   * Resends a fresh 6-digit OTP code to the phone number associated with the provided session.
   *
   * <p>Business rules enforced:
   *
   * <ol>
   *   <li>Validate that the session (sessionId) still exists in Redis.
   *   <li>Enforce a 60-second cooldown per session to prevent SMS abuse.
   *   <li>Generate a fresh OTP code and overwrite the old one in Redis, resetting its TTL.
   *   <li>Refresh the pending auth session TTL.
   *   <li>TODO: Delegate OTP delivery to SMS integration service.
   * </ol>
   *
   * @param request The resend OTP request containing a valid session ID. Must not be null.
   * @throws HttpException If session is not found or already expired (400 BAD_REQUEST).
   * @throws HttpException If resend is requested before cooldown period ends (429
   *     TOO_MANY_REQUESTS).
   */
  void resendOtp(ResendOtpRequest request);
}
