package ai.omnicure.iam.domain.entity;

import ai.omnicure.core.domain.entity.BaseEntity;


import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Getter
@Setter

@Table(name = "permissions")
public class Permission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "permission_id", updatable = false, nullable = false)
    private UUID permissionId;

    @Column(name = "permission_code", nullable = false, unique = true, length = 100)
    private String permissionCode;

    @Column(name = "permission_name", nullable = false, length = 200)
    private String permissionName;

    @Column(name = "module", length = 100)
    private String module;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Override
    public UUID getId() { return permissionId; }

    @Override
    public void setId(UUID id) { this.permissionId = id; }
}
