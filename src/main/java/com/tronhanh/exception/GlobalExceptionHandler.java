package com.tronhanh.exception;

import com.tronhanh.constant.MessageCodeConstant;
import com.tronhanh.dto.response.common.ApiResponse;
import com.tronhanh.dto.response.common.LocalizedMessageDto;
import com.tronhanh.util.MessageUtils;
import com.tronhanh.util.ResponseUtils;
import com.tronhanh.validation.EnumValue;
import com.tronhanh.validation.I18nField;
import com.tronhanh.validation.RequireField;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler providing centralized error responses across all REST controllers.
 * Processes custom {@link HttpException}, validation annotations ({@link RequireField}, {@link EnumValue}, {@link I18nField}),
 * and unhandled system exceptions into standardized {@link ApiResponse} envelopes.
 */
@RestControllerAdvice
public class GlobalExceptionHandler
{

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  /**
   * Handles custom application {@link HttpException}.
   *
   * @param ex the HttpException instance
   * @return ResponseEntity with standardized ApiResponse
   */
  @ExceptionHandler(HttpException.class)
  public ResponseEntity<ApiResponse<Object>> handleHttpException(HttpException ex) {
    logger.error("HttpException occurred: status={}, message={}", ex.getStatusCode(), ex.getLocalizedMessageDto());
    HttpStatus status = HttpStatus.resolve(ex.getStatusCode());
    if (Objects.isNull(status)) {
      status = HttpStatus.INTERNAL_SERVER_ERROR;
    }
    String messageCode = Objects.nonNull(ex.getLocalizedMessageDto()) ? ex.getLocalizedMessageDto().getVi() : MessageCodeConstant.MSG_CODE_105;
    return ResponseUtils.error(status, messageCode);
  }

  /**
   * Handles validation errors thrown during @Valid DTO request body processing.
   * Extracts {@link RequireField}, {@link EnumValue}, and {@link I18nField} meta-annotations to build localized messages.
   *
   * @param ex the MethodArgumentNotValidException instance
   * @return ResponseEntity with standardized ApiResponse
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    logger.error("MethodArgumentNotValidException occurred: {}", ex.getMessage());

    List<LocalizedMessageDto> errors = new ArrayList<>();

    for (ObjectError error : ex.getBindingResult().getAllErrors()) {
      try {
        ConstraintViolation<?> violation = error.unwrap(ConstraintViolation.class);
        if (Objects.nonNull(violation) && Objects.nonNull(violation.getConstraintDescriptor())) {
          var annotation = violation.getConstraintDescriptor().getAnnotation();
          if (annotation instanceof RequireField requireField) {
            String messageCode = requireField.messageCode();
            I18nField i18n = requireField.i18n();
            LocalizedMessageDto localizedMsg = MessageUtils.getMessageWithI18n(messageCode, i18n);
            errors.add(localizedMsg);
            continue;
          } else if (annotation instanceof EnumValue enumValue) {
            String messageCode = enumValue.messageCode();
            I18nField i18n = enumValue.i18n();
            LocalizedMessageDto localizedMsg = MessageUtils.getMessageWithI18n(messageCode, i18n);
            errors.add(localizedMsg);
            continue;
          }
        }
      } catch (Exception ignored) {
        // Fallback for non-unwrap violations
      }

      String defaultMsg = error.getDefaultMessage();
      if (Objects.nonNull(defaultMsg) && !defaultMsg.isBlank()) {
        errors.add(
            LocalizedMessageDto.builder()
                .vi(defaultMsg)
                .en(defaultMsg)
                .build());
      }
    }

    return ResponseUtils.error(HttpStatus.BAD_REQUEST, MessageCodeConstant.MSG_CODE_100, errors);
  }

  /**
   * Handles JSR-380 {@link ConstraintViolationException} for path variables and request params.
   *
   * @param ex the ConstraintViolationException instance
   * @return ResponseEntity with standardized ApiResponse
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiResponse<Object>> handleConstraintViolationException(
      ConstraintViolationException ex) {
    logger.error("ConstraintViolationException occurred: {}", ex.getMessage());

    List<LocalizedMessageDto> errors = new ArrayList<>();

    for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
      if (Objects.nonNull(violation.getConstraintDescriptor())) {
        var annotation = violation.getConstraintDescriptor().getAnnotation();
        if (annotation instanceof RequireField requireField) {
          String messageCode = requireField.messageCode();
          I18nField i18n = requireField.i18n();
          LocalizedMessageDto localizedMsg = MessageUtils.getMessageWithI18n(messageCode, i18n);
          errors.add(localizedMsg);
        } else if (annotation instanceof EnumValue enumValue) {
          String messageCode = enumValue.messageCode();
          I18nField i18n = enumValue.i18n();
          LocalizedMessageDto localizedMsg = MessageUtils.getMessageWithI18n(messageCode, i18n);
          errors.add(localizedMsg);
        } else {
          errors.add(
              LocalizedMessageDto.builder()
                  .vi(violation.getMessage())
                  .en(violation.getMessage())
                  .build());
        }
      }
    }

    return ResponseUtils.error(HttpStatus.BAD_REQUEST, MessageCodeConstant.MSG_CODE_100, errors);
  }

  /**
   * Fallback handler for unhandled exceptions.
   *
   * @param ex the Exception instance
   * @return ResponseEntity with standardized ApiResponse
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
    logger.error("Unhandled exception: ", ex);
    return ResponseUtils.error(
        HttpStatus.INTERNAL_SERVER_ERROR,
        MessageCodeConstant.MSG_CODE_105);
  }
}
