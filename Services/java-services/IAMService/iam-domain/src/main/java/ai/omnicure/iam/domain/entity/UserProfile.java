package ai.omnicure.iam.domain.entity;

import ai.omnicure.core.domain.entity.BaseEntity;


import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter

@Table(name = "user_profiles")
public class UserProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "profile_id", updatable = false, nullable = false)
    private UUID profileId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "full_name", length = 200)
    private String fullName;

    @Column(name = "gender", length = 20)
    private String gender;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Column(name = "biography", columnDefinition = "TEXT")
    private String biography;

    @Column(name = "timezone", length = 50)
    private String timezone = "UTC";

    @Override
    public UUID getId() { return profileId; }

    @Override
    public void setId(UUID id) { this.profileId = id; }
}
