package ai.omnicure.core.infra.repository;

import ai.omnicure.core.domain.repository.IReadRepository;
import ai.omnicure.core.shared.constant.KeyConstants;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;
import java.util.Optional;

public abstract class BaseReadRepository<T, ID> implements IReadRepository<T, ID> {

    @PersistenceContext(unitName = KeyConstants.ConnectionStrings.READ_ENTITY_MANAGER_FACTORY)
    protected EntityManager entityManager;

    protected final Class<T> entityClass;

    @SuppressWarnings("unchecked")
    protected BaseReadRepository() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<T> getById(ID id) {
        return Optional.ofNullable(entityManager.find(entityClass, id));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(ID id) {
        return getById(id).isPresent();
    }
}
