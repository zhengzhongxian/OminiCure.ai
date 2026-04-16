package ai.omnicure.iam.domain.entity;

import jakarta.persistence.*;



import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Getter
@Setter

@Table(name = "role_permissions",
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_role_permissions", columnNames = {"role_id", "permission_id"})
       })
public class RolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id", nullable = false)
    private Permission permission;

    @Version
    @Column(name = "row_version")
    private Long rowVersion;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
}
