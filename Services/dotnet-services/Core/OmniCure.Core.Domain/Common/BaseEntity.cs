namespace OmniCure.Core.Domain.Common;

public abstract class BaseEntity<TKey> : IAuditable, ISoftDelete
{
    public TKey Id { get; set; } = default!;
    
    public DateTime? CreatedAt { get; set; }
    
    public string? CreatedBy { get; set; }
    
    public DateTime? UpdatedAt { get; set; }
    
    public string? UpdatedBy { get; set; }
    
    public bool IsDeleted { get; set; }
    
    public DateTime? DeletedAt { get; set; }
    
    public string? DeletedBy { get; set; }
}
