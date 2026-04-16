package ai.omnicure.core.domain.repository;

import java.util.List;
import java.util.Optional;

public interface IWriteRepository<T, ID> {
    
    Optional<T> getById(ID id);
    
    T add(T entity);
    
    T update(T entity);
    
    void remove(T entity, boolean hardDelete);
    
    List<T> addRange(Iterable<T> entities);
    
    void removeRange(Iterable<T> entities, boolean hardDelete);

    boolean existsById(ID id);
}
