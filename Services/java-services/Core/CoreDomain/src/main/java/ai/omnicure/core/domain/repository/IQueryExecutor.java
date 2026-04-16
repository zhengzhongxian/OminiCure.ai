package ai.omnicure.core.domain.repository;

import ai.omnicure.core.shared.pagination.PagedList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IQueryExecutor {
    <T> PagedList<T> toPagedList(Iterable<T> items, long totalCount, int pageNumber, int pageSize);
}
