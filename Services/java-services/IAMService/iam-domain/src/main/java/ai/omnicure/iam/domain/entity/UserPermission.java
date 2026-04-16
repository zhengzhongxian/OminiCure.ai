package ai.omnicure.iam.domain.entity;

import jakarta.persistence.*;



import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Getter
@Setter

@Table(name = "user_permissions",
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_user_permissions", columnNames = {"user_id", "permission_id"})
       })
public class UserPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id", nullable = false)
    private Permission permission;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
}
