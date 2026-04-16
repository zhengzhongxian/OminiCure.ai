namespace OmniCure.Core.Domain.Cache;

public abstract class CacheEntry<T>
{
    protected readonly string Prefix;
    public T? Value { get; set; }

    protected CacheEntry(string prefix)
    {
        Prefix = prefix;
    }

    public string GetKey(string identifier)
        => Shared.Helpers.CacheKeyHelper.Resolve(Prefix, identifier);

    public static TEntry Load<T2, TEntry>(string prefix, T2? value = default)
        where TEntry : CacheEntry<T2>
    {
        var entry = (TEntry)Activator.CreateInstance(typeof(TEntry), prefix)!;
        entry.Value = value;
        return entry;
    }
}
