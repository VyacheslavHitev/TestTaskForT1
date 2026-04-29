package models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.SuperBuilder;
import org.json.JSONObject;

import java.lang.annotation.*;
import java.util.TimeZone;

@NoArgsConstructor
@SuperBuilder
public abstract class Entity implements AutoCloseable {

    public abstract Entity init();

    public abstract JSONObject toJson();

    protected abstract void create();

    protected abstract void delete();

    @SneakyThrows
    public static JSONObject serialize(Object object) {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        return new JSONObject(objectMapper.writeValueAsString(object));
    }

    @Override
    public void close() {
    }

    @Documented
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    protected @interface Ignore {
    }

}
