package com.tronhanh.controller;

import com.tronhanh.constant.MessageCodeConstant;
import com.tronhanh.dto.request.auth.LoginRequest;
import com.tronhanh.dto.request.auth.ResendOtpRequest;
import com.tronhanh.dto.request.auth.VerifyOtpRequest;
import com.tronhanh.dto.response.auth.LoginResponse;
import com.tronhanh.dto.response.auth.UserProfileResponse;
import com.tronhanh.dto.response.common.ApiResponse;
import com.tronhanh.service.AuthService;
import com.tronhanh.util.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller exposing REST endpoints for Enterprise 2FA Authentication, Token Rotation, Logout, and
 * User Profile.
 */
@Tag(
    name = "Authentication",
    description = "2FA Authentication, Token Refresh, and Session Management API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  /** Authentication service instance. */
  private final AuthService authService;

  /**
   * Primary authentication via phone number and password.
   *
   * @param request LoginRequest payload containing phone number and password.
   * @return ResponseEntity envelope containing LoginResponse with session ID.
   */
  @Operation(summary = "2FA Login via Phone Number and Password")
  @PostMapping("/login")
  public ResponseEntity<ApiResponse<LoginResponse>> login(
      @Valid @RequestBody LoginRequest request) {
    LoginResponse response = authService.login(request);
    return ResponseUtils.successWithData(response, MessageCodeConstant.MSG_CODE_001);
  }

  /**
   * OTP verification to issue Access Token and Refresh Token.
   *
   * @param request VerifyOtpRequest payload containing session ID and OTP code.
   * @return ResponseEntity envelope containing TokenResponse.
   */
  @Operation(summary = "2FA OTP Code Verification and JWT Token Issuance")
  @PostMapping("/verify-otp")
  public ResponseEntity<ApiResponse<Void>> verifyOtp(
      @Valid @RequestBody VerifyOtpRequest request, HttpServletResponse response) {
    authService.verifyOtp(request, response);
    return ResponseUtils.success(MessageCodeConstant.MSG_CODE_001);
  }

  /**
   * Refreshes JWT Access Token using active Refresh Token with Token Family Rotation.
   *
   * @param refreshToken Refresh Token retrieved from HttpOnly Cookie.
   * @param response HttpServletResponse to set the new Refresh Token cookie.
   * @return ResponseEntity envelope containing new TokenResponse.
   */
  @Operation(summary = "Refresh JWT Access Token using Refresh Token")
  @PostMapping("/refresh-token")
  public ResponseEntity<ApiResponse<Void>> refreshToken(
      @CookieValue(name = "refresh_token", required = false) String refreshToken,
      HttpServletResponse response) {
    authService.refreshToken(refreshToken, response);
    return ResponseUtils.success(MessageCodeConstant.MSG_CODE_001);
  }

  /**
   * Logs out user account, revoking Refresh Token family and blacklisting Access Token JTI.
   *
   * @param response HttpServletResponse to clear the Refresh Token cookie.
   * @param refreshToken Refresh Token retrieved from HttpOnly Cookie.
   * @return ResponseEntity envelope containing success response.
   */
  @Operation(summary = "Logout user account and revoke Refresh Token")
  @PostMapping("/logout")
  public ResponseEntity<ApiResponse<Void>> logout(
      HttpServletResponse response,
      @CookieValue(name = "access_token", required = false) String accessToken,
      @CookieValue(name = "refresh_token", required = false) String refreshToken) {

    authService.logout(accessToken, refreshToken, response);
    return ResponseUtils.success(MessageCodeConstant.MSG_CODE_001);
  }

  /**
   * Retrieves profile information of currently authenticated user.
   *
   * @return ResponseEntity envelope containing UserProfileResponse.
   */
  @Operation(summary = "Get profile information of currently authenticated user")
  @GetMapping("/me")
  public ResponseEntity<ApiResponse<UserProfileResponse>> getCurrentUserProfile() {
    UserProfileResponse response = authService.getCurrentUserProfile();
    return ResponseUtils.successWithData(response, MessageCodeConstant.MSG_CODE_001);
  }

  /**
   * Resends a fresh OTP code to the phone number associated with the active 2FA session.
   *
   * @param request ResendOtpRequest payload containing the active session ID.
   * @return ResponseEntity envelope containing success response.
   */
  @Operation(summary = "Resend OTP code for an active 2FA session")
  @PostMapping("/resend-otp")
  public ResponseEntity<ApiResponse<Void>> resendOtp(@Valid @RequestBody ResendOtpRequest request) {
    authService.resendOtp(request);
    return ResponseUtils.success(MessageCodeConstant.MSG_CODE_001);
  }

}
