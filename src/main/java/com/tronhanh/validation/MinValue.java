package com.tronhanh.validation;

import com.tronhanh.constant.MessageCodeConstant;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Custom constraint annotation checking that a numeric field is greater than or equal to a minimum value. */
@Documented
@Constraint(validatedBy = MinValueValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface MinValue {

  /**
   * Minimum allowed value.
   *
   * @return minimum value
   */
  long value();

  /**
   * Message code constant for validation failure.
   *
   * @return message code key
   */
  String messageCode() default MessageCodeConstant.MSG_CODE_100;

  /**
   * Field display name for message formatting.
   *
   * @return field name string
   */
  String field() default "";

  /**
   * Validation message template.
   *
   * @return message string
   */
  String message() default "";

  /**
   * Validation groups payload.
   *
   * @return group classes
   */
  Class<?>[] groups() default {};

  /**
   * Payload associated with the constraint.
   *
   * @return payload classes
   */
  Class<? extends Payload>[] payload() default {};
}
