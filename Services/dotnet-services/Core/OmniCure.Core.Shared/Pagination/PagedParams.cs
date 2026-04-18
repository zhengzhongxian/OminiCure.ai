namespace OmniCure.Core.Shared.Pagination;

public class PagedParams
{
    private int _pageSize = 10;

    public int PageNumber { get; set; } = 1;

    public int PageSize
    {
        get => _pageSize;
        set => _pageSize = value switch
        {
            > 1000 => 1000,
            < 1 => 1,
            _ => value
        };
    }

    public string? SearchKeyword { get; set; }

    public string? SortBy { get; set; } = default!;

    public string? SortDirection { get; set; } = "asc";
}
