package com.tronhanh.exception;

import com.tronhanh.constant.MessageCodeConstant;
import com.tronhanh.dto.response.common.ApiResponse;
import com.tronhanh.dto.response.common.ValidationErrorItem;
import com.tronhanh.dto.response.common.ValidationErrorResponse;
import com.tronhanh.util.MessageUtils;
import com.tronhanh.util.ResponseUtils;
import com.tronhanh.validation.EnumValue;
import com.tronhanh.validation.RequireField;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler providing centralized error responses across all REST controllers.
 * Processes custom {@link HttpException}, validation annotations ({@link RequireField}, {@link EnumValue}),
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
  public ResponseEntity<ValidationErrorResponse> handleHttpException(HttpException ex) {
    logger.error("HttpException occurred: status={}, message={}", ex.getStatusCode(), ex.getMessage());
    HttpStatus status = HttpStatus.resolve(ex.getStatusCode());
    if (Objects.isNull(status)) {
      status = HttpStatus.INTERNAL_SERVER_ERROR;
    }
    
    String messageCode = Objects.nonNull(ex.getMessageCode()) ? ex.getMessageCode() : MessageCodeConstant.MSG_CODE_105;
    String message;
    if (Objects.nonNull(ex.getArgs()) && ex.getArgs().length > 0) {
      message = MessageUtils.getMessage(messageCode, ex.getArgs());
    } else {
      message = MessageUtils.getMessage(messageCode);
    }

    List<ValidationErrorItem> errors = new ArrayList<>();
    errors.add(ValidationErrorItem.builder()
        .messageCode(messageCode)
        .message(message)
        .build());

    return ResponseEntity.status(status)
        .body(ValidationErrorResponse.builder()
            .status(status.value())
            .errors(errors)
            .timestamp(Instant.now())
            .traceId(ResponseUtils.getTraceId())
            .path(ResponseUtils.getRequestPath())
            .build());
  }

  /**
   * Handles validation errors thrown during @Valid DTO request body processing.
   * Extracts {@link RequireField} and {@link EnumValue} annotations to build English validation messages.
   *
   * @param ex the MethodArgumentNotValidException instance
   * @return ResponseEntity with standardized ApiResponse
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    logger.error("MethodArgumentNotValidException occurred: {}", ex.getMessage());

    List<ValidationErrorItem> errors = new ArrayList<>();

    for (ObjectError error : ex.getBindingResult().getAllErrors()) {
      try {
        ConstraintViolation<?> violation = error.unwrap(ConstraintViolation.class);
        if (Objects.nonNull(violation) && Objects.nonNull(violation.getConstraintDescriptor())) {
          var annotation = violation.getConstraintDescriptor().getAnnotation();
          if (annotation instanceof RequireField requireField) {
            // Extract specific messageCode and field name from @RequireField annotation
            String messageCode = requireField.messageCode();
            String fieldName = !requireField.field().isBlank()
                ? requireField.field()
                : violation.getPropertyPath().toString();
            String msg = MessageUtils.getMessage(messageCode, fieldName);
            errors.add(ValidationErrorItem.builder().messageCode(messageCode).message(msg).build());
            continue;
          } else if (annotation instanceof EnumValue enumValue) {
            // Extract specific messageCode and field name from @EnumValue annotation
            String messageCode = enumValue.messageCode();
            String fieldName = !enumValue.field().isBlank()
                ? enumValue.field()
                : violation.getPropertyPath().toString();
            String msg = MessageUtils.getMessage(messageCode, fieldName);
            errors.add(ValidationErrorItem.builder().messageCode(messageCode).message(msg).build());
            continue;
          } else if (annotation instanceof com.tronhanh.validation.MinValue minValue) {
            // Extract specific messageCode and field name from @MinValue annotation
            String messageCode = minValue.messageCode();
            String fieldName = !minValue.field().isBlank()
                ? minValue.field()
                : violation.getPropertyPath().toString();
            String msg = MessageUtils.getMessage(messageCode, fieldName);
            errors.add(ValidationErrorItem.builder().messageCode(messageCode).message(msg).build());
            continue;
          }
        }
      } catch (Exception ignored) {
        // Fallback for non-unwrappable violations
      }

      // Fallback: use the default message from the annotation
      String defaultMsg = error.getDefaultMessage();
      if (Objects.nonNull(defaultMsg) && !defaultMsg.isBlank()) {
        if (defaultMsg.startsWith("MSG_CODE_")) {
          errors.add(ValidationErrorItem.builder()
              .messageCode(defaultMsg)
              .message(MessageUtils.getMessage(defaultMsg))
              .build());
        } else {
          errors.add(ValidationErrorItem.builder()
              .messageCode(MessageCodeConstant.MSG_CODE_100)
              .message(defaultMsg)
              .build());
        }
      }
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ValidationErrorResponse.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .errors(errors)
            .timestamp(Instant.now())
            .traceId(ResponseUtils.getTraceId())
            .path(ResponseUtils.getRequestPath())
            .build());
  }

  /**
   * Handles JSR-380 {@link ConstraintViolationException} for path variables and request params.
   *
   * @param ex the ConstraintViolationException instance
   * @return ResponseEntity with standardized ApiResponse
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ValidationErrorResponse> handleConstraintViolationException(
      ConstraintViolationException ex) {
    logger.error("ConstraintViolationException occurred: {}", ex.getMessage());

    List<ValidationErrorItem> errors = new ArrayList<>();

    for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
      if (Objects.nonNull(violation.getConstraintDescriptor())) {
        var annotation = violation.getConstraintDescriptor().getAnnotation();
        if (annotation instanceof RequireField requireField) {
          // Extract messageCode and field name from @RequireField annotation
          String messageCode = requireField.messageCode();
          String fieldName = !requireField.field().isBlank()
              ? requireField.field()
              : violation.getPropertyPath().toString();
          String msg = MessageUtils.getMessage(messageCode, fieldName);
          errors.add(ValidationErrorItem.builder().messageCode(messageCode).message(msg).build());
        } else if (annotation instanceof EnumValue enumValue) {
          // Extract messageCode and field name from @EnumValue annotation
          String messageCode = enumValue.messageCode();
          String fieldName = !enumValue.field().isBlank()
              ? enumValue.field()
              : violation.getPropertyPath().toString();
          String msg = MessageUtils.getMessage(messageCode, fieldName);
          errors.add(ValidationErrorItem.builder().messageCode(messageCode).message(msg).build());
        } else if (annotation instanceof com.tronhanh.validation.MinValue minValue) {
          // Extract messageCode and field name from @MinValue annotation
          String messageCode = minValue.messageCode();
          String fieldName = !minValue.field().isBlank()
              ? minValue.field()
              : violation.getPropertyPath().toString();
          String msg = MessageUtils.getMessage(messageCode, fieldName);
          errors.add(ValidationErrorItem.builder().messageCode(messageCode).message(msg).build());
        } else {
          // Generic fallback for other constraint types
          String defaultMsg = violation.getMessage();
          if (Objects.nonNull(defaultMsg) && defaultMsg.startsWith("MSG_CODE_")) {
            errors.add(ValidationErrorItem.builder()
                .messageCode(defaultMsg)
                .message(MessageUtils.getMessage(defaultMsg))
                .build());
          } else {
            errors.add(ValidationErrorItem.builder()
                .messageCode(MessageCodeConstant.MSG_CODE_100)
                .message(defaultMsg)
                .build());
          }
        }
      }
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ValidationErrorResponse.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .errors(errors)
            .timestamp(Instant.now())
            .traceId(ResponseUtils.getTraceId())
            .path(ResponseUtils.getRequestPath())
            .build());
  }

  /**
   * Handles JSON unreadable exceptions, such as passing unknown properties in payload.
   *
   * @param ex the HttpMessageNotReadableException instance
   * @return ResponseEntity with standardized ApiResponse
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ValidationErrorResponse> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException ex) {
    logger.error("HttpMessageNotReadableException occurred: {}", ex.getMessage());

    List<ValidationErrorItem> errors = new ArrayList<>();
    errors.add(ValidationErrorItem.builder()
        .messageCode(MessageCodeConstant.MSG_CODE_212)
        .message(MessageUtils.getMessage(MessageCodeConstant.MSG_CODE_212))
        .build());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ValidationErrorResponse.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .errors(errors)
            .timestamp(Instant.now())
            .traceId(ResponseUtils.getTraceId())
            .path(ResponseUtils.getRequestPath())
            .build());
  }

  /**
   * Fallback handler for unhandled exceptions.
   *
   * @param ex the Exception instance
   * @return ResponseEntity with standardized ApiResponse
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ValidationErrorResponse> handleGenericException(Exception ex) {
    logger.error("Unhandled exception: ", ex);

    List<ValidationErrorItem> errors = new ArrayList<>();
    errors.add(ValidationErrorItem.builder()
        .messageCode(MessageCodeConstant.MSG_CODE_105)
        .message(MessageUtils.getMessage(MessageCodeConstant.MSG_CODE_105))
        .build());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ValidationErrorResponse.builder()
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .errors(errors)
            .timestamp(Instant.now())
            .traceId(ResponseUtils.getTraceId())
            .path(ResponseUtils.getRequestPath())
            .build());
  }
}
