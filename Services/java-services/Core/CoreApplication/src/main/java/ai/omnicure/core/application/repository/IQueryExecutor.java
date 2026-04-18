package ai.omnicure.core.application.repository;

import ai.omnicure.core.shared.pagination.PagedList;

public interface IQueryExecutor {
    <T> PagedList<T> toPagedList(Iterable<T> items, long totalCount, int pageNumber, int pageSize);
}
