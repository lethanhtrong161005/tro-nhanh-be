package com.tronhanh.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tronhanh.constant.AppConstant;
import com.tronhanh.constant.MessageCodeConstant;
import com.tronhanh.dto.response.common.ValidationErrorResponse;
import com.tronhanh.entity.UserEntity;
import com.tronhanh.repository.UserRepository;
import com.tronhanh.service.RedisService;
import com.tronhanh.util.ResponseUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter to extract Bearer JWT token, verify against JTI-based Redis blacklist, and authenticate
 * requests.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  /** JWT provider component. */
  private final JwtProvider jwtProvider;

  /** User persistence repository. */
  private final UserRepository userRepository;

  /** Global Redis service wrapper. */
  private final RedisService redisService;

  /** JSON object mapper component. */
  private final ObjectMapper objectMapper;

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    AntPathMatcher pathMatcher = new AntPathMatcher();
    String path = request.getServletPath();
    return Arrays.stream(AppConstant.PUBLIC_ENDPOINTS).anyMatch(p -> pathMatcher.match(p, path));
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    try {
      String jwt = parseJwt(request);
      if (Objects.nonNull(jwt) && jwtProvider.validateToken(jwt)) {
        String jti = jwtProvider.getJtiFromToken(jwt);

        // 1. Check if token's JTI is blacklisted in Redis
        if (Objects.nonNull(jti) && redisService.hasKey(AppConstant.BLACKLIST_JTI_PREFIX + jti)) {
          log.warn("Rejected blacklisted access token JTI: {}", jti);
          sendUnauthorizedError(response, MessageCodeConstant.MSG_CODE_203);
          return;
        }

        // 2. Set Security Context
        UUID userId = jwtProvider.getUserIdFromToken(jwt);
        UserEntity user = userRepository.findByUserIdAndIsDeletedFalse(userId).orElse(null);

        if (Objects.nonNull(user)) {
          Claims claims = jwtProvider.getClaimsFromToken(jwt);
          String roleName = claims.get("role", String.class);

          List<SimpleGrantedAuthority> authorities =
              Objects.nonNull(roleName)
                  ? Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + roleName))
                  : Collections.emptyList();

          UsernamePasswordAuthenticationToken authentication =
              new UsernamePasswordAuthenticationToken(user, null, authorities);
          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }
    } catch (Exception e) {
      log.error("Cannot set user authentication: {}", e.getMessage(), e);
    }

    filterChain.doFilter(request, response);
  }

  /**
   * Parses Bearer token string from HTTP request Authorization header.
   *
   * @param request HttpServletRequest instance.
   * @return JWT token string or null if absent.
   */
  private String parseJwt(HttpServletRequest request) {
    if (request.getCookies() != null) {
      for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
        if ("access_token".equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    }

    String headerAuth = request.getHeader("Authorization");
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
      return headerAuth.substring(7);
    }
    return null;
  }

  /**
   * Writes standardized JSON 401 Unauthorized response to client output stream.
   *
   * @param response HttpServletResponse instance.
   * @param messageCode Error message code key.
   * @throws IOException If writing to output stream fails.
   */
  private void sendUnauthorizedError(HttpServletResponse response, String messageCode)
      throws IOException {
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");

    ResponseEntity<ValidationErrorResponse> errorResponse =
        ResponseUtils.error(HttpStatus.UNAUTHORIZED, messageCode);
    response.getWriter().write(objectMapper.writeValueAsString(errorResponse.getBody()));
  }
}
