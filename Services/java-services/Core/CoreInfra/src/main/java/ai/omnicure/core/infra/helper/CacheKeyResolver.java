package ai.omnicure.core.infra.helper;

public final class CacheKeyResolver {

    private CacheKeyResolver() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String resolve(String prefix, String identifier) {
        return (prefix + ":" + identifier).toLowerCase();
    }
}
