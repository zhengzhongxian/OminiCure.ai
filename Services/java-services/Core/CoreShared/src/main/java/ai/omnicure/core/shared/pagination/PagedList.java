package ai.omnicure.core.shared.pagination;

import lombok.Getter;

import java.util.List;

@Getter
public class PagedList<T> {

    private final int currentPage;
    private final int totalPages;
    private final int pageSize;
    private final long totalCount;
    private final boolean hasPrevious;
    private final boolean hasNext;
    private final List<T> items;

    public PagedList(List<T> items, long count, int pageNumber, int pageSize) {
        this.items = items;
        this.totalCount = count;
        this.currentPage = pageNumber;
        this.pageSize = pageSize;
        this.totalPages = (int) Math.ceil((double) count / pageSize);
        this.hasPrevious = currentPage > 1;
        this.hasNext = currentPage < totalPages;
    }

    public static <T> PagedList<T> of(List<T> items, long count, int pageNumber, int pageSize) {
        return new PagedList<>(items, count, pageNumber, pageSize);
    }
}
