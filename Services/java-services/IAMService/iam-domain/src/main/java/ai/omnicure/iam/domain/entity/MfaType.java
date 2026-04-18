package ai.omnicure.iam.domain.entity;

import ai.omnicure.iam.domain.enums.MfaMechanism;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Getter
@Setter

@Table(name = "mfa_types")
public class MfaType {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "type_id", updatable = false, nullable = false)
    private UUID typeId;

    @Column(name = "type_name", nullable = false, unique = true, length = 50)
    private String typeName;

    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    @Enumerated(EnumType.STRING)
    @Column(name = "mechanism_type", nullable = false, length = 20)
    private MfaMechanism mechanismType;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
