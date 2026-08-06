package com.tronhanh.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Meta-annotation defining field display labels in Vietnamese and English for i18n validation messages.
 */
@Documented
@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface I18nField {

  /**
   * Vietnamese display label for the field.
   *
   * @return label string
   */
  String vi() default "";

  /**
   * English display label for the field.
   *
   * @return label string
   */
  String en() default "";
}
