package ai.omnicure.iam.domain.entity;

import ai.omnicure.core.domain.entity.BaseEntity;


import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Getter
@Setter

@Table(name = "roles", indexes = {
    @Index(name = "idx_roles_role_code", columnList = "role_code"),
    @Index(name = "idx_roles_is_active", columnList = "is_active")
})
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "role_id", updatable = false, nullable = false)
    private UUID roleId;

    @Column(name = "role_name", nullable = false, unique = true, length = 100)
    private String roleName;

    @Column(name = "role_code", nullable = false, unique = true, length = 50)
    private String roleCode;

    @Column(name = "priority", nullable = false)
    private Integer priority = 0;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Override
    public UUID getId() { return roleId; }

    @Override
    public void setId(UUID id) { this.roleId = id; }
}
