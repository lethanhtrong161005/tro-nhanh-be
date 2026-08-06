package com.tronhanh.controller;

import com.tronhanh.constant.MessageCodeConstant;
import com.tronhanh.dto.request.auth.LoginRequest;
import com.tronhanh.dto.request.auth.LogoutRequest;
import com.tronhanh.dto.request.auth.RefreshTokenRequest;
import com.tronhanh.dto.request.auth.VerifyOtpRequest;
import com.tronhanh.dto.response.auth.LoginResponse;
import com.tronhanh.dto.response.auth.TokenResponse;
import com.tronhanh.dto.response.auth.UserProfileResponse;
import com.tronhanh.dto.response.common.ApiResponse;
import com.tronhanh.service.AuthService;
import com.tronhanh.util.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller exposing REST endpoints for Enterprise 2FA Authentication, Token Rotation, Logout, and User Profile.
 */
@Tag(name = "Authentication", description = "2FA Authentication, Token Refresh, and Session Management API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController
{

  /**
   * Authentication service instance.
   */
  private final AuthService authService;

  /**
   * Step 1 primary authentication via phone number and password.
   *
   * @param request LoginRequest payload containing phone number and password.
   * @return ResponseEntity envelope containing LoginResponse with session ID.
   */
  @Operation(summary = "Step 1 2FA Login via Phone Number and Password")
  @PostMapping("/login")
  public ResponseEntity<ApiResponse<LoginResponse>> login(
      @Valid @RequestBody LoginRequest request) {
    LoginResponse response = authService.login(request);
    return ResponseUtils.successWithData(response, MessageCodeConstant.MSG_CODE_001);
  }

  /**
   * Step 2 OTP verification to issue Access Token and Refresh Token.
   *
   * @param request VerifyOtpRequest payload containing session ID and OTP code.
   * @return ResponseEntity envelope containing TokenResponse.
   */
  @Operation(summary = "Step 2 2FA OTP Code Verification and JWT Token Issuance")
  @PostMapping("/verify-otp")
  public ResponseEntity<ApiResponse<TokenResponse>> verifyOtp(
      @Valid @RequestBody VerifyOtpRequest request) {
    TokenResponse response = authService.verifyOtp(request);
    return ResponseUtils.successWithData(response, MessageCodeConstant.MSG_CODE_001);
  }

  /**
   * Refreshes JWT Access Token using active Refresh Token with Token Family Rotation.
   *
   * @param request RefreshTokenRequest payload containing Refresh Token.
   * @return ResponseEntity envelope containing new TokenResponse.
   */
  @Operation(summary = "Refresh JWT Access Token using Refresh Token")
  @PostMapping("/refresh-token")
  public ResponseEntity<ApiResponse<TokenResponse>> refreshToken(
      @Valid @RequestBody RefreshTokenRequest request) {
    TokenResponse response = authService.refreshToken(request);
    return ResponseUtils.successWithData(response, MessageCodeConstant.MSG_CODE_001);
  }

  /**
   * Logs out user account, revoking Refresh Token family and blacklisting Access Token JTI.
   *
   * @param httpRequest HttpServletRequest to extract Authorization header.
   * @param request LogoutRequest payload containing Refresh Token.
   * @return ResponseEntity envelope containing success response.
   */
  @Operation(summary = "Logout user account and revoke Refresh Token")
  @PostMapping("/logout")
  public ResponseEntity<ApiResponse<Void>> logout(
      HttpServletRequest httpRequest,
      @Valid @RequestBody LogoutRequest request) {
    String authHeader = httpRequest.getHeader("Authorization");
    authService.logout(authHeader, request);
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
}
