package ai.omnicure.core.infra.cache;

import ai.omnicure.core.infra.helper.CacheKeyResolver;
import ai.omnicure.core.infra.helper.JsonHelper;
import java.lang.reflect.Constructor;

public abstract class CacheEntry<T> {

    protected final String prefix;
    private T value;

    protected CacheEntry(String prefix) {
        this.prefix = prefix;
    }

    public T getValue() { return value; }

    public void setValue(T value) { this.value = value; }

    public String getKey(String identifier) {
        return CacheKeyResolver.resolve(prefix, identifier);
    }

    public String toJson() {
        return value != null ? JsonHelper.serializeSafe(value) : "";
    }

    @Override
    public String toString() {
        return toJson();
    }

    @SuppressWarnings("unchecked")
    public static <T, TEntry extends CacheEntry<T>> TEntry load(Class<TEntry> entryClass, String prefix, T value) {
        try {
            Constructor<TEntry> constructor = entryClass.getDeclaredConstructor(String.class);
            constructor.setAccessible(true);
            TEntry entry = constructor.newInstance(prefix);
            entry.setValue(value);
            return entry;
        } catch (Exception e) {
            throw new IllegalStateException("Cannot create CacheEntry instance of " + entryClass.getName(), e);
        }
    }

    public static <T, TEntry extends CacheEntry<T>> TEntry load(Class<TEntry> entryClass, String prefix) {
        return load(entryClass, prefix, null);
    }
}
