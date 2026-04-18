package ai.omnicure.iam.infrastructure.repository;

import ai.omnicure.core.infra.repository.BaseWriteRepository;
import ai.omnicure.iam.application.repository.IUserWriteRepository;
import ai.omnicure.iam.domain.entity.User;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserWriteRepository extends BaseWriteRepository<User, UUID> implements IUserWriteRepository {

    @Override
    public Optional<User> findByUserNameOrEmail(String usernameOrEmail) {
        try {
            User user = entityManager.createQuery(
                    "SELECT u FROM User u WHERE (u.userName = :input OR u.email = :input) AND u.isDeleted = false",
                    User.class)
                    .setParameter("input", usernameOrEmail)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
