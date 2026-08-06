package com.tronhanh.dto.specification;

import com.tronhanh.enums.SearchOperation;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.Collection;
import java.util.Objects;
import org.springframework.data.jpa.domain.Specification;

/**
 * Enterprise JPA {@link Specification} implementation supporting nested paths and type-safe predicates.
 *
 * @param <T> entity type
 */
public class GenericSpecification<T> implements Specification<T> {

  private final FilterCriteria criteria;

  /**
   * GenericSpecification constructor.
   *
   * @param criteria filter criteria container
   */
  public GenericSpecification(FilterCriteria criteria) {
    this.criteria = criteria;
  }

  /**
   * Translates FilterCriteria into a JPA Criteria API Predicate.
   *
   * @param root query root entity
   * @param query criteria query instance
   * @param cb criteria builder instance
   * @return constructed Predicate
   */
  @Override
  public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
    if (Objects.isNull(criteria)
        || Objects.isNull(criteria.getKey())
        || criteria.getKey().isBlank()) {
      return cb.conjunction();
    }

    SearchOperation op = criteria.getOperation();
    if (Objects.isNull(op)) {
      return cb.conjunction();
    }

    Path<?> path = getPath(root, criteria.getKey());
    Object value = criteria.getValue();

    switch (op) {
      case EQUAL:
        return Objects.isNull(value) ? cb.isNull(path) : cb.equal(path, value);

      case NOT_EQUAL:
        return Objects.isNull(value) ? cb.isNotNull(path) : cb.notEqual(path, value);

      case LIKE:
        if (Objects.isNull(value)) {
          return cb.conjunction();
        }
        return cb.like(cb.lower(path.as(String.class)), "%" + value.toString().toLowerCase() + "%");

      case STARTS_WITH:
        if (Objects.isNull(value)) {
          return cb.conjunction();
        }
        return cb.like(cb.lower(path.as(String.class)), value.toString().toLowerCase() + "%");

      case ENDS_WITH:
        if (Objects.isNull(value)) {
          return cb.conjunction();
        }
        return cb.like(cb.lower(path.as(String.class)), "%" + value.toString().toLowerCase());

      case GREATER_THAN:
        if (Objects.isNull(value)) {
          return cb.conjunction();
        }
        return cb.greaterThan(path.as(String.class), value.toString());

      case GREATER_THAN_EQUAL:
        if (Objects.isNull(value)) {
          return cb.conjunction();
        }
        return cb.greaterThanOrEqualTo(path.as(String.class), value.toString());

      case LESS_THAN:
        if (Objects.isNull(value)) {
          return cb.conjunction();
        }
        return cb.lessThan(path.as(String.class), value.toString());

      case LESS_THAN_EQUAL:
        if (Objects.isNull(value)) {
          return cb.conjunction();
        }
        return cb.lessThanOrEqualTo(path.as(String.class), value.toString());

      case IN:
        if (value instanceof Collection<?> collection && !collection.isEmpty()) {
          return path.in(collection);
        }
        return cb.conjunction();

      case NOT_IN:
        if (value instanceof Collection<?> collection && !collection.isEmpty()) {
          return cb.not(path.in(collection));
        }
        return cb.conjunction();

      case IS_NULL:
        return cb.isNull(path);

      case IS_NOT_NULL:
        return cb.isNotNull(path);

      default:
        return cb.conjunction();
    }
  }

  private Path<?> getPath(Root<T> root, String attributeName) {
    if (attributeName.contains(".")) {
      String[] parts = attributeName.split("\\.");
      Path<?> path = root.get(parts[0]);
      for (int i = 1; i < parts.length; i++) {
        path = path.get(parts[i]);
      }
      return path;
    }
    return root.get(attributeName);
  }
}
