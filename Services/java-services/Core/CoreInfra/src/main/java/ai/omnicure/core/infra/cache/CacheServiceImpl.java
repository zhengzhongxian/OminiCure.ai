package ai.omnicure.core.infra.cache;

import ai.omnicure.core.application.cache.ICacheService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements ICacheService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final RedisMessageListenerContainer listenerContainer;

    @Override
    public void setString(String key, String value, Duration expiry) {
        redisTemplate.opsForValue().set(key, value, expiry);
    }

    @Override
    public <T> void set(String key, T value, Duration expiry) {
        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, json, expiry);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize cache value", e);
        }
    }

    @Override
    public String getString(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public <T> T get(String key, Class<T> type) {
        String json = redisTemplate.opsForValue().get(key);
        if (json == null)
            return null;
        try {
            return objectMapper.readValue(json, type);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize cache value", e);
        }
    }

    @Override
    public <T> T getOrSet(String key, Supplier<T> factory, Duration expiry, Class<T> type) {
        T cached = get(key, type);
        if (cached != null)
            return cached;
        T value = factory.get();
        set(key, value, expiry);
        return value;
    }

    @Override
    public void remove(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void removeByPrefix(String prefix) {
        Set<String> keys = redisTemplate.keys(prefix + "*");
        if (!keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    @Override
    public boolean acquireLock(String lockKey, String lockValue, Duration expiry) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, expiry);
        return Boolean.TRUE.equals(result);
    }

    @Override
    public boolean releaseLock(String lockKey, String lockValue) {
        String script = """
                if redis.call('get', KEYS[1]) == ARGV[1] then
                    return redis.call('del', KEYS[1])
                else
                    return 0
                end
                """;
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        Long result = redisTemplate.execute(redisScript, List.of(lockKey), lockValue);
        return result == 1L;
    }

    @Override
    public void publish(String channel, String message) {
        redisTemplate.convertAndSend(channel, message);
    }

    @Override
    public void subscribe(String channel, Consumer<String> handler) {
        listenerContainer.addMessageListener(
                (message, pattern) -> handler.accept(new String(message.getBody())),
                new ChannelTopic(channel));
    }

    @Override
    public void hashSet(String key, Map<String, String> fields, Duration expiry) {
        redisTemplate.opsForHash().putAll(key, fields);
        if (expiry != null) {
            redisTemplate.expire(key, expiry);
        }
    }

    @Override
    public Map<String, String> hashGetAll(String key) {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        Map<String, String> result = new HashMap<>();
        entries.forEach((k, v) -> result.put(k.toString(), v.toString()));
        return result;
    }

    @Override
    public String hashGet(String key, String field) {
        Object value = redisTemplate.opsForHash().get(key, field);
        return value != null ? value.toString() : null;
    }

    @Override
    public void hashDelete(String key, String field) {
        redisTemplate.opsForHash().delete(key, field);
    }
}
