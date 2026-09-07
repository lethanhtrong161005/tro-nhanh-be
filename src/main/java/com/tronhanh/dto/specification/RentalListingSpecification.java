package com.tronhanh.dto.specification;

import com.tronhanh.dto.request.listing.RentalListingSearchRequest;
import com.tronhanh.entity.RentalListingEntity;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import org.springframework.data.jpa.domain.Specification;

/** Centralizes dynamic search and filter predicates for rental listing queries. */
public final class RentalListingSpecification {

  private RentalListingSpecification() {
    // Utility class
  }

  /**
   * Combines keyword search and every supplied filter with AND predicates.
   *
   * @param request Optional listing search and filter parameters.
   * @return Specification containing all applicable query predicates.
   */
  public static Specification<RentalListingEntity> build(RentalListingSearchRequest request) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      // Soft-deleted listings must never be returned by read APIs.
      predicates.add(criteriaBuilder.isFalse(root.get("isDeleted")));
      if (Objects.isNull(request)) {
        return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
      }

      // A keyword matches either title or description without case sensitivity.
      if (hasText(request.getKeyword())) {
        String keywordPattern = toContainsPattern(request.getKeyword());
        predicates.add(
            criteriaBuilder.or(
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("title")), keywordPattern, '\\'),
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("description")), keywordPattern, '\\')));
      }

      // All fixed filters are optional and compose with the keyword predicate using AND.
      if (hasText(request.getLocation())) {
        predicates.add(
            criteriaBuilder.like(
                criteriaBuilder.lower(root.get("location")),
                toContainsPattern(request.getLocation()),
                '\\'));
      }
      if (Objects.nonNull(request.getTypeId())) {
        predicates.add(
            criteriaBuilder.equal(
                root.join("type", JoinType.INNER).get("typeId"), request.getTypeId()));
      }
      if (Objects.nonNull(request.getMinPrice())) {
        predicates.add(
            criteriaBuilder.greaterThanOrEqualTo(root.get("price"), request.getMinPrice()));
      }
      if (Objects.nonNull(request.getMaxPrice())) {
        predicates.add(
            criteriaBuilder.lessThanOrEqualTo(root.get("price"), request.getMaxPrice()));
      }
      if (Objects.nonNull(request.getStatus())) {
        predicates.add(criteriaBuilder.equal(root.get("status"), request.getStatus()));
      }

      return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
    };
  }

  private static boolean hasText(String value) {
    return Objects.nonNull(value) && !value.isBlank();
  }

  private static String toContainsPattern(String value) {
    return "%" + value.trim().toLowerCase(Locale.ROOT) + "%";
  }
}
