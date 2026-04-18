package ai.omnicure.iam.domain.entity;

import ai.omnicure.iam.domain.enums.AuthProviderName;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Getter
@Setter

@Table(name = "auth_providers",
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_auth_providers", columnNames = {"provider_name", "provider_subject_id"})
       })
public class AuthProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "provider_id", updatable = false, nullable = false)
    private UUID providerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_name", nullable = false, length = 50)
    private AuthProviderName providerName;

    @Column(name = "provider_subject_id", nullable = false, length = 255)
    private String providerSubjectId;
}
