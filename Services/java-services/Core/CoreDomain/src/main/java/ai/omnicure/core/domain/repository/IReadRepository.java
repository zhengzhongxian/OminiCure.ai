package ai.omnicure.core.domain.repository;

import java.util.Optional;

public interface IReadRepository<T, ID> {
    Optional<T> getById(ID id);

    boolean existsById(ID id);
}
