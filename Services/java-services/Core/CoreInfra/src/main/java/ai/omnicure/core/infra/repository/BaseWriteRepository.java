package ai.omnicure.core.infra.repository;

import ai.omnicure.core.domain.entity.BaseEntity;
import ai.omnicure.core.domain.repository.IWriteRepository;
import ai.omnicure.core.shared.constant.KeyConstants;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class BaseWriteRepository<T, ID> implements IWriteRepository<T, ID> {

    @PersistenceContext(unitName = KeyConstants.ConnectionStrings.WRITE_ENTITY_MANAGER_FACTORY)
    protected EntityManager entityManager;

    protected final Class<T> entityClass;

    @SuppressWarnings("unchecked")
    protected BaseWriteRepository() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public Optional<T> getById(ID id) {
        return Optional.ofNullable(entityManager.find(entityClass, id));
    }

    @Override
    @Transactional
    public T add(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    @Transactional
    public T update(T entity) {
        return entityManager.merge(entity);
    }

    @Override
    @Transactional
    public void remove(T entity, boolean hardDelete) {
        if (hardDelete) {
            entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
            return;
        }

        if (entity instanceof BaseEntity baseEntity) {
            baseEntity.setIsDeleted(true);
            baseEntity.setDeletedAt(LocalDateTime.now());
            entityManager.merge(baseEntity);
        } else {
            entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
        }
    }

    @Override
    @Transactional
    public List<T> addRange(Iterable<T> entities) {
        List<T> result = new ArrayList<>();
        for (T entity : entities) {
            entityManager.persist(entity);
            result.add(entity);
        }
        return result;
    }

    @Override
    @Transactional
    public void removeRange(Iterable<T> entities, boolean hardDelete) {
        for (T entity : entities) {
            remove(entity, hardDelete);
        }
    }

    @Override
    public boolean existsById(ID id) {
        return getById(id).isPresent();
    }
}
