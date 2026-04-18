package ai.omnicure.iam.domain.entity;

import ai.omnicure.iam.domain.enums.DisplayMode;
import ai.omnicure.iam.domain.enums.Language;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter

@Table(name = "user_settings")
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "setting_id", updatable = false, nullable = false)
    private UUID settingId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "choice_theme", length = 50)
    private DisplayMode choiceTheme = DisplayMode.LIGHT;

    @Enumerated(EnumType.STRING)
    @Column(name = "choice_language", length = 10)
    private Language choiceLanguage = Language.EN;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

