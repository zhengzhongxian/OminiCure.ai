namespace OmniCure.Core.Shared.Helpers;

public static class CacheKeyHelper
{
    public static string Resolve(string prefix, string identifier)
        => $"{prefix}:{identifier}".ToLowerInvariant();
}
