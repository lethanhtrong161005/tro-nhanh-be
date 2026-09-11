package com.tronhanh.entity;

import com.tronhanh.annotation.UuidV7;
import com.tronhanh.enums.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing extended user profile details mapped to database table user_profiles.
 * Maintains a strict 1-to-1 relationship with {@link UserEntity}.
 *
 * @see BaseEntity
 * @see UserEntity
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_profiles")
public class UserProfileEntity extends BaseEntity
{

  /** Unique identifier for this user profile record. */
  @Id
  @UuidV7
  @Column(name = "user_profile_id", updatable = false, nullable = false)
  private UUID userProfileId;

  /** Associated UserEntity owning this profile. */
  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false, unique = true)
  private UserEntity user;

  /** S3 URL referencing user's avatar image. */
  @Column(name = "avatar_url", length = 500)
  private String avatarUrl;

  /** User date of birth. */
  @Column(name = "date_of_birth")
  private LocalDate dateOfBirth;

  /** User gender identity. */
  @Enumerated(EnumType.STRING)
  @Column(name = "gender", length = 20)
  private Gender gender;

  /** Short biography or personal introduction. */
  @Column(name = "bio", length = 500)
  private String bio;

  /** User occupation or professional title. */
  @Column(name = "occupation", length = 100)
  private String occupation;

  /** Street address line. */
  @Column(name = "address", length = 255)
  private String address;

  /** City or province name. */
  @Column(name = "city", length = 100)
  private String city;

  /** District name. */
  @Column(name = "district", length = 100)
  private String district;

  /** Ward or commune name. */
  @Column(name = "ward", length = 100)
  private String ward;
}