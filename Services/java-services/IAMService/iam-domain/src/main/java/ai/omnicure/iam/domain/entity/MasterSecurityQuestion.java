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

@Table(name = "master_security_questions")
public class MasterSecurityQuestion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "question_id", updatable = false, nullable = false)
    private UUID questionId;

    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Override
    public UUID getId() { return questionId; }

    @Override
    public void setId(UUID id) { this.questionId = id; }
}
