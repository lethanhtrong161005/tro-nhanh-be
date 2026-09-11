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
 * Request payload for partial update (PATCH) of user profile details.
 * Only non-null fields will be updated in the existing user profile.
 */
@Schema(description = "Request payload for partial user profile update")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatchUserProfileRequest
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
  @Schema(description = "User gender (MALE, FEMALE, OTHER)", example = "FEMALE")
  private Gender gender;

  /** Biography or self-introduction. */
  @Schema(
      description = "Short biography or introduction",
      example = "Passionate designer looking for a room with good lighting.")
  @Size(max = 500, message = "Bio must not exceed 500 characters")
  private String bio;

  /** Current occupation or profession. */
  @Schema(description = "User occupation or job title", example = "UI/UX Designer")
  @Size(max = 100, message = "Occupation must not exceed 100 characters")
  private String occupation;

  /** Street address line. */
  @Schema(description = "Street address line", example = "456 Le Duan Boulevard")
  @Size(max = 255, message = "Address must not exceed 255 characters")
  private String address;

  /** City or province. */
  @Schema(description = "City or province name", example = "Da Nang")
  @Size(max = 100, message = "City must not exceed 100 characters")
  private String city;

  /** District name. */
  @Schema(description = "District name", example = "Hai Chau District")
  @Size(max = 100, message = "District must not exceed 100 characters")
  private String district;

  /** Ward or commune name. */
  @Schema(description = "Ward or commune name", example = "Thach Thang Ward")
  @Size(max = 100, message = "Ward must not exceed 100 characters")
  private String ward;
}