package com.tronhanh.dto.request.profile;

import com.tronhanh.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request payload for full update (PUT) of user profile details.
 * Also used to initialize a new profile if none exists for the authenticated user.
 */
@Schema(description = "Request payload for full user profile update")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserProfileRequest
{

  /** S3 URL of the user avatar image. */
  @Schema(
      description = "S3 URL referencing user avatar image",
      example = "https://s3.ap-southeast-1.amazonaws.com/tronhanh/avatars/users/avatar123.jpg")
  @Size(max = 500, message = "Avatar URL must not exceed 500 characters")
  private String avatarUrl;

  /** User date of birth. Must be in the past or present. */
  @Schema(description = "Date of birth (YYYY-MM-DD)", example = "2000-01-15")
  @PastOrPresent(message = "Date of birth must not be in the future")
  private LocalDate dateOfBirth;

  /** User gender identity. */
  @Schema(description = "User gender (MALE, FEMALE, OTHER)", example = "MALE")
  private Gender gender;

  /** Biography or self-introduction. */
  @Schema(
      description = "Short biography or introduction",
      example = "Software engineering student looking for a quiet room near university.")
  @Size(max = 500, message = "Bio must not exceed 500 characters")
  private String bio;

  /** Current occupation or profession. */
  @Schema(description = "User occupation or job title", example = "Student")
  @Size(max = 100, message = "Occupation must not exceed 100 characters")
  private String occupation;

  /** Street address line. */
  @Schema(description = "Street address line", example = "123 Nguyen Trai Street")
  @Size(max = 255, message = "Address must not exceed 255 characters")
  private String address;

  /** City or province. */
  @Schema(description = "City or province name", example = "Ho Chi Minh City")
  @Size(max = 100, message = "City must not exceed 100 characters")
  private String city;

  /** District name. */
  @Schema(description = "District name", example = "District 1")
  @Size(max = 100, message = "District must not exceed 100 characters")
  private String district;

  /** Ward or commune name. */
  @Schema(description = "Ward or commune name", example = "Ben Nghe Ward")
  @Size(max = 100, message = "Ward must not exceed 100 characters")
  private String ward;
}