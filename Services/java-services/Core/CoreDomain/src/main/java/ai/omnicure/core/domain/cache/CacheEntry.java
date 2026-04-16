package ai.omnicure.core.domain.cache;

import ai.omnicure.core.shared.helper.CacheKeyHelper;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Constructor;

@Getter
@Setter
public abstract class CacheEntry<T> {

    protected final String prefix;
    private T value;

    protected CacheEntry(String prefix) {
        this.prefix = prefix;
    }

    public String getKey(String identifier) {
        return CacheKeyHelper.resolve(prefix, identifier);
    }

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
