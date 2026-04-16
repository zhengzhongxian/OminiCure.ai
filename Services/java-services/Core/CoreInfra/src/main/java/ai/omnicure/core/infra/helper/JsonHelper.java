package ai.omnicure.core.infra.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class JsonHelper {

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private JsonHelper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String serialize(Object obj) throws JsonProcessingException {
        if (obj == null) return null;
        return OBJECT_MAPPER.writeValueAsString(obj);
    }

    public static String serializeSafe(Object obj) {
        try {
            return serialize(obj);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    public static <T> T deserialize(String json, Class<T> clazz) throws JsonProcessingException {
        if (json == null || json.isBlank()) return null;
        return OBJECT_MAPPER.readValue(json, clazz);
    }

    public static <T> T deserializeSafe(String json, Class<T> clazz) {
        try {
            return deserialize(json, clazz);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static boolean isValidJson(String json) {
        try {
            OBJECT_MAPPER.readTree(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String prettyPrint(String json) {
        try {
            Object obj = OBJECT_MAPPER.readTree(json);
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return json;
        }
    }
}
