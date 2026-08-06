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
 * Custom constraint annotation checking that a string field is non-null and non-blank.
 */
@Documented
@Constraint(validatedBy = RequireFieldValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireField {

  /**
   * Message code constant for internationalized validation failure.
   *
   * @return message code key
   */
  String messageCode() default MessageCodeConstant.MSG_CODE_200;

  /**
   * Field display label meta-annotation for localized %s message formatting.
   *
   * @return I18nField meta-annotation
   */
  I18nField i18n() default @I18nField;

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
