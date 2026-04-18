package ai.omnicure.iam.application.repository;

import ai.omnicure.core.domain.repository.IWriteRepository;
import ai.omnicure.iam.domain.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface IUserWriteRepository extends IWriteRepository<User, UUID> {

    Optional<User> findByUserNameOrEmail(String usernameOrEmail);
}
