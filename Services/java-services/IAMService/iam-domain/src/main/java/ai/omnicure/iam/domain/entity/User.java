package ai.omnicure.iam.domain.entity;

import ai.omnicure.core.domain.entity.BaseEntity;


import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter

@Table(name = "users", indexes = {
    @Index(name = "idx_users_user_name", columnList = "user_name"),
    @Index(name = "idx_users_email", columnList = "email"),
    @Index(name = "idx_users_status", columnList = "status")
})
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", updatable = false, nullable = false)
    private UUID userId;

    @Column(name = "user_name", nullable = false, unique = true, length = 100)
    private String userName;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "password_change_at")
    private LocalDateTime passwordChangeAt;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE";

    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified = false;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "mfa_enabled", nullable = false)
    private Boolean mfaEnabled = false;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Override
    public UUID getId() { return userId; }

    @Override
    public void setId(UUID id) { this.userId = id; }
}
