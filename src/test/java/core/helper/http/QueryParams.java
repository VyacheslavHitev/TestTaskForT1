package core.helper.http;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import models.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class QueryParams {
    private final transient Map<String, String> map = new HashMap<>();

    public QueryParams add(Object key, Object value) {
        if (value.getClass().isArray())
            for (Object e : (Object[]) value)
                map.put(key.toString(), e.toString());
        else
            map.put(key.toString(), value.toString());
        return this;
    }

    public QueryParams() {
    }

    public QueryParams(Object... keysAndValues) {
        for (int i = 0; i < keysAndValues.length; i = i + 2) {
            map.put(keysAndValues[i].toString(), keysAndValues[i + 1].toString());
        }
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("&", "?", "");
        map.forEach((k, v) -> joiner.add(k + "=" + v));
        Entity.serialize(this).toMap().forEach(this::add);
        return joiner.toString().length() == 1 ? "" : joiner.toString();
    }

    public Map<String, String> toMap() {
        Entity.serialize(this).toMap().forEach(this::add);
        return map;
    }
}
