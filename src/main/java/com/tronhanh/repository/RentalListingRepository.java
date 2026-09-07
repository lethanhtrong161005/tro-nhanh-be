package com.tronhanh.repository;

import com.tronhanh.entity.RentalListingEntity;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/** Spring Data repository for paginated and dynamic rental listing queries. */
@Repository
public interface RentalListingRepository
    extends JpaRepository<RentalListingEntity, UUID>,
        JpaSpecificationExecutor<RentalListingEntity> {

  /**
   * Executes a paginated specification and fetches listing type in the same query.
   *
   * @param specification Dynamic listing query predicates.
   * @param pageable Pagination and sorting configuration.
   * @return Page of matching rental listings.
   */
  @Override
  @EntityGraph(attributePaths = "type")
  Page<RentalListingEntity> findAll(
      Specification<RentalListingEntity> specification, Pageable pageable);
}
