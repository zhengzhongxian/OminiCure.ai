package ai.omnicure.core.shared.pagination;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagedParams {

    private int pageNumber = 1;
    private int pageSize = 10;
    private String searchKeyword;
    private String sortBy;
    private String sortDirection = "asc";

    public void setPageSize(int pageSize) {
        if (pageSize > 1000) {
            this.pageSize = 1000;
        } else if (pageSize < 1) {
            this.pageSize = 1;
        } else {
            this.pageSize = pageSize;
        }
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = Math.max(pageNumber, 1);
    }
}
