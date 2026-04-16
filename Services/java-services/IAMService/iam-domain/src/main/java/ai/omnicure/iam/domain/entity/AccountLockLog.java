package ai.omnicure.iam.domain.entity;

import jakarta.persistence.*;



import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter

@Table(name = "account_lock_logs")
public class AccountLockLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "log_id", updatable = false, nullable = false)
    private UUID logId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "reason_code", length = 50)
    private String reasonCode;

    @Column(name = "locked_at", nullable = false)
    private LocalDateTime lockedAt;

    @Column(name = "unlock_at")
    private LocalDateTime unlockAt;

    @Column(name = "unlocked_at")
    private LocalDateTime unlockedAt;

    @Column(name = "unlocked_by", length = 100)
    private String unlockedBy;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;
}
