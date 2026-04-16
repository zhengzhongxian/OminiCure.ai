package ai.omnicure.core.shared.helper;

public final class CacheKeyHelper {

    private CacheKeyHelper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String resolve(String prefix, String identifier) {
        return (prefix + ":" + identifier).toLowerCase();
    }
}
