package com.tronhanh.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tronhanh.dto.request.listing.RentalListingSearchRequest;
import com.tronhanh.dto.response.common.PageResponse;
import com.tronhanh.dto.response.listing.RentalListingResponse;
import com.tronhanh.entity.RentalListingEntity;
import com.tronhanh.entity.RentalListingTypeEntity;
import com.tronhanh.enums.RentalListingStatus;
import com.tronhanh.exception.HttpException;
import com.tronhanh.helper.SearchHelper;
import com.tronhanh.repository.RentalListingRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/** Unit tests for {@link RentalListingServiceImpl}. */
@ExtendWith(MockitoExtension.class)
class RentalListingServiceImplTest {

  @Mock private RentalListingRepository rentalListingRepository;

  @Mock private SearchHelper searchHelper;

  @InjectMocks private RentalListingServiceImpl rentalListingService;

  /** Verifies page metadata and listing type mapping for a paginated result. */
  @Test
  void getAllListingsReturnsMappedPage() {
    UUID typeId = UUID.randomUUID();
    RentalListingTypeEntity listingType =
        RentalListingTypeEntity.builder().typeId(typeId).name("Studio").build();
    RentalListingEntity listing =
        RentalListingEntity.builder()
            .listingId(UUID.randomUUID())
            .type(listingType)
            .title("Modern home")
            .location("Hồ Chí Minh")
            .address("District 1")
            .area(new BigDecimal("28.50"))
            .price(new BigDecimal("5500000"))
            .status(RentalListingStatus.ACTIVE)
            .isBoosted(true)
            .build();
    listing.setCreatedAt(Instant.parse("2026-09-03T00:00:00Z"));
    PageImpl<RentalListingEntity> listingPage =
        new PageImpl<>(List.of(listing), PageRequest.of(0, 10), 1);
    when(rentalListingRepository.findAll(
            org.mockito.ArgumentMatchers.<Specification<RentalListingEntity>>any(),
            any(Pageable.class)))
        .thenReturn(listingPage);

    PageResponse<RentalListingResponse> response =
        rentalListingService.getAllListings(new RentalListingSearchRequest());

    assertEquals(0, response.getPageNumber());
    assertEquals(10, response.getPageSize());
    assertEquals(1, response.getTotalElements());
    assertEquals(typeId, response.getContent().getFirst().getTypeId());
    assertEquals("Studio", response.getContent().getFirst().getTypeName());
  }

  /** Verifies that an inverted price range is rejected before repository access. */
  @Test
  void getAllListingsRejectsInvalidPriceRange() {
    RentalListingSearchRequest request =
        RentalListingSearchRequest.builder()
            .minPrice(new BigDecimal("9000000"))
            .maxPrice(new BigDecimal("1000000"))
            .build();

    assertThrows(HttpException.class, () -> rentalListingService.getAllListings(request));

    verify(rentalListingRepository, never()).findAll(
        org.mockito.ArgumentMatchers.<Specification<RentalListingEntity>>any(),
        any(Pageable.class));
  }
}
