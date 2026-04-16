package ai.omnicure.core.infra.repository;

import ai.omnicure.core.domain.repository.IQueryExecutor;
import ai.omnicure.core.shared.pagination.PagedList;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class QueryExecutor implements IQueryExecutor {

    @Override
    public <T> PagedList<T> toPagedList(Iterable<T> items, long totalCount, int pageNumber, int pageSize) {
        List<T> list = new ArrayList<>();
        items.forEach(list::add);
        return new PagedList<>(list, totalCount, pageNumber, pageSize);
    }

    public <T> PagedList<T> fromPage(Page<T> page) {
        return new PagedList<>(
            page.getContent(),
            page.getTotalElements(),
            page.getNumber() + 1,
            page.getSize()
        );
    }
}
