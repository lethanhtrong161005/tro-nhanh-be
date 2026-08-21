package com.tronhanh.util;

import com.tronhanh.constant.AppConstant;
import com.tronhanh.constant.MessageCodeConstant;
import com.tronhanh.dto.response.common.ApiResponse;
import com.tronhanh.dto.response.common.ValidationErrorItem;
import com.tronhanh.dto.response.common.ValidationErrorResponse;
import java.time.Instant;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Utility class for constructing standardized {@link ApiResponse} envelopes. Supports localized
 * message resolution and MDC request trace injection.
 */
public final class ResponseUtils {

  private ResponseUtils() {
    // Utility class
  }

  /**
   * Constructs a successful ResponseEntity with payload and a parameterized message code.
   *
   * @param <T> data payload type
   * @param data payload object
   * @param messageCode key in message bundle
   * @param args message parameters for %s substitution
   * @return ResponseEntity containing ApiResponse envelope
   */
  public static <T> ResponseEntity<ApiResponse<T>> successWithData(
      T data, String messageCode, Object... args) {
    String localizedMessage = MessageUtils.getLocalizedText(messageCode, args);
    return ResponseEntity.ok(
        ApiResponse.<T>builder()
            .status(HttpStatus.OK.value())
            .messageCode(messageCode)
            .message(localizedMessage)
            .data(data)
            .timestamp(Instant.now())
            .traceId(getTraceId())
            .path(getRequestPath())
            .build());
  }

  /**
   * Constructs a successful ResponseEntity with payload and default MSG_CODE_001 message.
   *
   * @param <T> data payload type
   * @param data payload object
   * @return ResponseEntity containing ApiResponse envelope
   */
  public static <T> ResponseEntity<ApiResponse<T>> successWithData(T data) {
    return successWithData(data, MessageCodeConstant.MSG_CODE_001);
  }

  /**
   * Constructs a successful ResponseEntity without data payload using a parameterized message code.
   *
   * @param <T> data payload type
   * @param messageCode key in message bundle
   * @param args message parameters for %s substitution
   * @return ResponseEntity containing ApiResponse envelope
   */
  public static <T> ResponseEntity<ApiResponse<T>> success(String messageCode, Object... args) {
    return successWithData(null, messageCode, args);
  }

  /**
   * Constructs a successful ResponseEntity without payload and default MSG_CODE_001 message.
   *
   * @param <T> data payload type
   * @return ResponseEntity containing ApiResponse envelope
   */
  public static <T> ResponseEntity<ApiResponse<T>> success() {
    return success(MessageCodeConstant.MSG_CODE_001);
  }

  /**
   * Constructs an error ResponseEntity with HTTP status, message code, and args.
   *
   * @param <T> data payload type
   * @param status HTTP status code
   * @param messageCode key in message bundle
   * @param args message parameters for %s substitution
   * @return ResponseEntity containing ApiResponse envelope
   */
  public static ResponseEntity<ValidationErrorResponse> error(
      HttpStatus status, String messageCode, Object... args) {
    String localizedMessage = MessageUtils.getLocalizedText(messageCode, args);
    List<ValidationErrorItem> errors = new ArrayList<>();
    errors.add(ValidationErrorItem.builder()
        .messageCode(messageCode)
        .message(localizedMessage)
        .build());
        
    return ResponseEntity.status(status)
        .body(
            ValidationErrorResponse.builder()
                .status(status.value())
                .errors(errors)
                .timestamp(Instant.now())
                .traceId(getTraceId())
                .path(getRequestPath())
                .build());
  }

  /**
   * Constructs an error ResponseEntity with HTTP status, message code, error details list, and
   * args.
   *
   * @param status HTTP status code
   * @param messageCode key in message bundle
   * @param errors list of detailed error objects
   * @param args message parameters for %s substitution
   * @return ResponseEntity containing ApiResponse envelope
   */
  public static ResponseEntity<ValidationErrorResponse> error(
      HttpStatus status, String messageCode, List<ValidationErrorItem> errors, Object... args) {
    return ResponseEntity.status(status)
        .body(
            ValidationErrorResponse.builder()
                .status(status.value())
                .errors(errors)
                .timestamp(Instant.now())
                .traceId(getTraceId())
                .path(getRequestPath())
                .build());
  }

  /**
   * Returns current MDC trace ID.
   *
   * @return trace ID string
   */
  public static String getTraceId() {
    String traceId = MDC.get(AppConstant.TRACE_ID_KEY);
    return Objects.nonNull(traceId) ? traceId : "";
  }

  /**
   * Retrieves current HTTP request path.
   *
   * @return request path string
   */
  public static String getRequestPath() {
    try {
      ServletRequestAttributes attributes =
          (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
      if (attributes != null) {
        return attributes.getRequest().getRequestURI();
      }
    } catch (Exception e) {
      // Ignored
    }
    return "";
  }
}
