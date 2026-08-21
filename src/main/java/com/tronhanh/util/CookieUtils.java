package com.tronhanh.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import com.tronhanh.constant.AppConstant;

/** Utility class for managing HTTP Cookies securely. */
public final class CookieUtils {

  private CookieUtils() {
    // Utility class
  }

  /**
   * Adds an HttpOnly, Secure, SameSite=Strict cookie to the response.
   *
   * @param response The HTTP Servlet Response
   * @param name The cookie name
   * @param value The cookie value
   * @param maxAgeSeconds Maximum age of the cookie in seconds
   */
  public static void addHttpOnlyCookie(
      HttpServletResponse response, String name, String value, long maxAgeSeconds) {
    ResponseCookie cookie =
        ResponseCookie.from(name, value)
            .httpOnly(true)
            .secure(true) // Should be true in production (requires HTTPS)
            .sameSite("Strict") // Prevents CSRF
            .path("/")
            .maxAge(maxAgeSeconds)
            .build();
    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
  }

  /**
   * Clears a cookie from the client by setting its maxAge to 0.
   *
   * @param response The HTTP Servlet Response
   * @param name The cookie name to clear
   */
  public static void clearCookie(HttpServletResponse response, String name) {
    ResponseCookie cookie =
        ResponseCookie.from(name, "")
            .httpOnly(true)
            .secure(true)
            .sameSite("Strict")
            .path("/")
            .maxAge(0)
            .build();
    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
  }

  /**
   * Helper method to set both Access Token and Refresh Token cookies safely. This logic is
   * extracted here so it can be reused for OAuth2 (Google Login, etc).
   *
   * @param response The HTTP Servlet Response
   * @param accessToken The JWT Access Token
   * @param refreshToken The JWT Refresh Token
   */
  public static void setAuthCookies(
      HttpServletResponse response, String accessToken, String refreshToken) {
    // Access token valid for 1 hour
    addHttpOnlyCookie(response, "access_token", accessToken, 3600);
    // Refresh token valid for configured TTL
    addHttpOnlyCookie(
        response,
        "refresh_token",
        refreshToken,
        AppConstant.REFRESH_FAMILY_TTL_DAYS * 24 * 60 * 60);
  }

  /**
   * Helper method to clear authentication cookies (used for logout).
   *
   * @param response The HTTP Servlet Response
   */
  public static void clearAuthCookies(HttpServletResponse response) {
    clearCookie(response, "access_token");
    clearCookie(response, "refresh_token");
  }
}
