package com.tronhanh.dto.specification;

import com.tronhanh.enums.SearchOperation;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.data.jpa.domain.Specification;

/**
 * Fluent builder constructing composite Spring Data JPA {@link Specification} queries.
 *
 * @param <T> entity type
 */
public class GenericSpecificationBuilder<T> {

  private final List<FilterCriteria> params = new ArrayList<>();

  /**
   * Adds a filter criterion if value is non-null and non-blank.
   *
   * @param key attribute name or nested path
   * @param op search operation
   * @param value filter target value
   * @return builder instance for chaining
   */
  public GenericSpecificationBuilder<T> with(String key, SearchOperation op, Object value) {
    if (Objects.nonNull(op)
        && Objects.nonNull(key)
        && !key.isBlank()
        && (op == SearchOperation.IS_NULL
            || op == SearchOperation.IS_NOT_NULL
            || (Objects.nonNull(value) && !value.toString().isBlank()))) {
      params.add(new FilterCriteria(key, op, value));
    }
    return this;
  }

  /**
   * Builds combined Specification linked via AND predicates.
   *
   * @return composite Specification instance
   */
  public Specification<T> buildAnd() {
    if (params.isEmpty()) {
      return null;
    }
    Specification<T> result = new GenericSpecification<>(params.get(0));
    for (int i = 1; i < params.size(); i++) {
      result = result.and(new GenericSpecification<>(params.get(i)));
    }
    return result;
  }

  /**
   * Builds combined Specification linked via OR predicates.
   *
   * @return composite Specification instance
   */
  public Specification<T> buildOr() {
    if (params.isEmpty()) {
      return null;
    }
    Specification<T> result = new GenericSpecification<>(params.get(0));
    for (int i = 1; i < params.size(); i++) {
      result = result.or(new GenericSpecification<>(params.get(i)));
    }
    return result;
  }
}
