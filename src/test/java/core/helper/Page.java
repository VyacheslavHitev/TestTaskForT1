package core.helper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;
import java.util.stream.Stream;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Page<C> {

    @JsonProperty("request_id")
    private String requestId;
    private List<C> list;
    private Meta meta;

    public Stream<C> stream(){
        return list.stream();
    }

    public C first() {
        return list.get(0);
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Meta {
        Integer totalCount;
        String next;
        String previous;
        Boolean hasMore;
        Boolean isApproximateTotal;
        Integer page;
        Integer perPage;
        Object details;
    }
}
