package com.tronhanh.validation;

import com.tronhanh.constant.MessageCodeConstant;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom constraint annotation verifying that a field value matches a valid Enum constant.
 */
@Documented
@Constraint(validatedBy = EnumValueValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumValue {

  /**
   * Target Enum class to validate against.
   *
   * @return enum class
   */
  Class<? extends Enum<?>> enumClass();

  /**
   * Message code constant for validation failure.
   *
   * @return message code key
   */
  String messageCode() default MessageCodeConstant.MSG_CODE_204;

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
