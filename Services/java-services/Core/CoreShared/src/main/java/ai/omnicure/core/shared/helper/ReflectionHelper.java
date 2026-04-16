package ai.omnicure.core.shared.helper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public final class ReflectionHelper {

    private ReflectionHelper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static <T> List<T> getConstants(Class<?> classType, Class<T> fieldType) {
        List<T> results = new ArrayList<>();
        getConstantsRecursive(classType, fieldType, results);
        return results.stream().distinct().toList();
    }

    public static List<String> getStringConstants(Class<?> classType) {
        return getConstants(classType, String.class);
    }

    public static Map<String, Object> getAllConstants(Class<?> classType) {
        Map<String, Object> results = new LinkedHashMap<>();
        getAllConstantsRecursive(classType, "", results);
        return results;
    }

    private static <T> void getConstantsRecursive(Class<?> classType, Class<T> fieldType, List<T> results) {
        for (Field field : classType.getDeclaredFields()) {
            int mod = field.getModifiers();
            if (Modifier.isPublic(mod) && Modifier.isStatic(mod) && Modifier.isFinal(mod)
                    && fieldType.isAssignableFrom(field.getType())) {
                try {
                    field.setAccessible(true);
                    results.add(fieldType.cast(field.get(null)));
                } catch (IllegalAccessException ignored) {}
            }
        }
        for (Class<?> nested : classType.getDeclaredClasses()) {
            getConstantsRecursive(nested, fieldType, results);
        }
    }

    private static void getAllConstantsRecursive(Class<?> classType, String prefix, Map<String, Object> results) {
        for (Field field : classType.getDeclaredFields()) {
            int mod = field.getModifiers();
            if (Modifier.isPublic(mod) && Modifier.isStatic(mod) && Modifier.isFinal(mod)) {
                try {
                    field.setAccessible(true);
                    results.putIfAbsent(prefix + field.getName(), field.get(null));
                } catch (IllegalAccessException ignored) {}
            }
        }
        for (Class<?> nested : classType.getDeclaredClasses()) {
            getAllConstantsRecursive(nested, prefix + nested.getSimpleName() + ".", results);
        }
    }
}
