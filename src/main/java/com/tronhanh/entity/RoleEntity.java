package com.tronhanh.entity;

import com.tronhanh.enums.RoleName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing user authorization roles in the system.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class RoleEntity extends BaseEntity
{

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "role_id", updatable = false, nullable = false)
  private UUID roleId;

  @Enumerated(EnumType.STRING)
  @Column(name = "role_name", nullable = false, unique = true, length = 50)
  private RoleName roleName;

  @Column(name = "description_vi")
  private String descriptionVi;

  @Column(name = "description_en")
  private String descriptionEn;

  @OneToMany(mappedBy = "role")
  @Builder.Default
  private List<SystemRoleAssignmentEntity> systemRoleAssignments = new ArrayList<>();
}
