package ai.omnicure.iam.domain.entity;

import jakarta.persistence.*;



import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Getter
@Setter

@Table(name = "user_roles",
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_user_roles", columnNames = {"user_id", "role_id"})
       },
       indexes = {
           @Index(name = "idx_user_roles_user_id", columnList = "user_id"),
           @Index(name = "idx_user_roles_role_id", columnList = "role_id")
       })
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_roles_user"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_roles_role"))
    private Role role;

    @Version
    @Column(name = "row_version")
    private Long rowVersion;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
}
