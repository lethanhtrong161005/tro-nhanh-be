package com.tronhanh.dto.response.profile;

import com.tronhanh.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response payload representing full profile and account details of a user.
 * Excludes sensitive credential data and internal soft-delete flags.
 */
@Schema(description = "Response payload containing user profile details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse
{

  /** Unique identifier of the user profile record. */
  @Schema(
      description = "Unique user profile ID",
      example = "0191bb95-923f-7b02-8d76-e17f7b112233")
  private UUID userProfileId;

  /** Unique identifier of the associated user account. */
  @Schema(
      description = "Associated user ID",
      example = "0191bb95-923f-7b02-8d76-e17f7b445566")
  private UUID userId;

  /** User primary email address. */
  @Schema(description = "User email address", example = "user@tronhanh.vn")
  private String email;

  /** User first name. */
  @Schema(description = "User first name", example = "Trong")
  private String firstName;

  /** User last name. */
  @Schema(description = "User last name", example = "Le")
  private String lastName;

  /** User display full name. */
  @Schema(description = "User full name", example = "Le Thanh Trong")
  private String fullName;

  /** User registered phone number. */
  @Schema(description = "User phone number", example = "0987654321")
  private String phoneNumber;

  /** S3 URL referencing user avatar image. */
  @Schema(
      description = "S3 URL referencing user avatar image",
      example = "https://s3.ap-southeast-1.amazonaws.com/tronhanh/avatars/users/avatar123.jpg")
  private String avatarUrl;

  /** User date of birth. */
  @Schema(description = "Date of birth (YYYY-MM-DD)", example = "2000-01-15")
  private LocalDate dateOfBirth;

  /** User gender identity. */
  @Schema(description = "User gender (MALE, FEMALE, OTHER)", example = "MALE")
  private Gender gender;

  /** Short biography or introduction. */
  @Schema(
      description = "Short biography or introduction",
      example = "Software engineering student looking for a quiet room near university.")
  private String bio;

  /** Current occupation or profession. */
  @Schema(description = "User occupation or job title", example = "Student")
  private String occupation;

  /** Street address line. */
  @Schema(description = "Street address line", example = "123 Nguyen Trai Street")
  private String address;

  /** City or province name. */
  @Schema(description = "City or province name", example = "Ho Chi Minh City")
  private String city;

  /** District name. */
  @Schema(description = "District name", example = "District 1")
  private String district;

  /** Ward or commune name. */
  @Schema(description = "Ward or commune name", example = "Ben Nghe Ward")
  private String ward;

  /** Profile creation timestamp. */
  @Schema(description = "Record creation timestamp", example = "2026-08-01T12:00:00Z")
  private Instant createdAt;

  /** Profile last modification timestamp. */
  @Schema(description = "Record last modification timestamp", example = "2026-08-05T15:30:00Z")
  private Instant updatedAt;
}