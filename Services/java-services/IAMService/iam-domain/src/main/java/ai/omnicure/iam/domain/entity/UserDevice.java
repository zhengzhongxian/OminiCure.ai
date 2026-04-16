package ai.omnicure.iam.domain.entity;

import jakarta.persistence.*;



import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter

@Table(name = "user_devices")
public class UserDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "device_id", updatable = false, nullable = false)
    private UUID deviceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "device_name", length = 200)
    private String deviceName;

    @Column(name = "device_type", length = 50)
    private String deviceType;

    @Column(name = "browser_fingerprint", length = 255)
    private String browserFingerprint;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "is_trusted", nullable = false)
    private Boolean isTrusted = false;

    @Column(name = "trust_until")
    private LocalDateTime trustUntil;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
