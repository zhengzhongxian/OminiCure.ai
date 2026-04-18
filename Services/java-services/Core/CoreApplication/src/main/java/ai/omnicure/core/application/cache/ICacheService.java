package ai.omnicure.core.application.cache;

import java.time.Duration;
import java.util.Map;
import java.util.function.Supplier;

public interface ICacheService {

    void setString(String key, String value, Duration expiry);

    <T> void set(String key, T value, Duration expiry);

    String getString(String key);

    <T> T get(String key, Class<T> type);

    <T> T getOrSet(String key, Supplier<T> factory, Duration expiry, Class<T> type);

    void remove(String key);

    void removeByPrefix(String prefix);

    boolean acquireLock(String lockKey, String lockValue, Duration expiry);

    boolean releaseLock(String lockKey, String lockValue);

    void publish(String channel, String message);

    void subscribe(String channel, java.util.function.Consumer<String> handler);

    void hashSet(String key, Map<String, String> fields, Duration expiry);

    Map<String, String> hashGetAll(String key);

    String hashGet(String key, String field);

    void hashDelete(String key, String field);
}
