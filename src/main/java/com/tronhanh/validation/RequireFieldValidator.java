package com.tronhanh.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * Constraint validator implementation enforcing non-null and non-blank rules for {@link
 * RequireField}. Supports validation for objects of any type (checks for null). If the type
 * is String, it additionally checks for non-blank.
 */
public class RequireFieldValidator implements ConstraintValidator<RequireField, Object> {

  /**
   * Validates whether the object is non-null. If it is a string, ensures it is non-blank.
   *
   * @param value object value to validate
   * @param context validator context
   * @return true if valid, false otherwise
   */
  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    if (Objects.isNull(value)) {
      return false;
    }
    if (value instanceof String strValue) {
      return !strValue.isBlank();
    }
    return true;
  }
}
