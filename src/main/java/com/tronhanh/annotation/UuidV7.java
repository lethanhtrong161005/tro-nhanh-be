package com.tronhanh.annotation;

import com.tronhanh.util.CommonUtil;
import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.hibernate.annotations.IdGeneratorType;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

/**
 * Marks a field to be generated using UUIDv7 (Time-Ordered Epoch). This annotation configures
 * Hibernate to use the nested UuidV7.Generator.
 */
@IdGeneratorType(UuidV7.Generator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface UuidV7 {

  /**
   * Custom Hibernate IdentifierGenerator that generates a UUIDv7 using the time-ordered epoch
   * approach.
   */
  class Generator implements IdentifierGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
      return CommonUtil.generateUuidV7();
    }
  }
}
