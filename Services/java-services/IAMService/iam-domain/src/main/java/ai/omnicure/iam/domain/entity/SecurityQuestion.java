package ai.omnicure.iam.domain.entity;

import jakarta.persistence.*;



import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter

@Table(name = "security_questions",
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_security_questions_user", columnNames = {"user_id", "question_id"})
       })
public class SecurityQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private MasterSecurityQuestion question;

    @Column(name = "answer_hash", nullable = false, length = 255)
    private String answerHash;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
}
