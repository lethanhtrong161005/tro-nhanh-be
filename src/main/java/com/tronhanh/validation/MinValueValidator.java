package com.tronhanh.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * Constraint validator implementation enforcing a minimum numeric value for {@link MinValue}.
 * Null values are ignored (considered valid) to allow @RequireField to handle nullability.
 */
public class MinValueValidator implements ConstraintValidator<MinValue, Number> {

  private long minValue;

  @Override
  public void initialize(MinValue constraintAnnotation) {
    this.minValue = constraintAnnotation.value();
  }

  @Override
  public boolean isValid(Number value, ConstraintValidatorContext context) {
    if (Objects.isNull(value)) {
      return true; // Use @RequireField to enforce non-null
    }
    return value.longValue() >= minValue;
  }

}
