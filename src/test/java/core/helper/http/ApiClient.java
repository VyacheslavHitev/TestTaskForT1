package core.helper.http;

import core.helper.Page;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;
import org.junit.jupiter.api.Assertions;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

public class ApiClient<T> {
    private final RequestSpecification requestSpecification;
    private Call call;
    private TypeRef<T> typeRef;
    Object[] args;

    public ApiClient(RequestSpecification requestSpecification) {
        this.requestSpecification = RestAssured.with().spec(requestSpecification);
    }

    public ApiClient<T> model(Class<T> clazz) {
        this.typeRef = new TypeRef<T>() {
            @Override
            public Type getType() {
                return clazz;
            }
        };
        return this;
    }

    public ApiClient<T> model(TypeRef<T> typeRef) {
        this.typeRef = typeRef;
        return this;
    }

    public Response api(Call call, Object... args) {
        requestSpecification.baseUri(call.url);
        this.call = call;
        this.args = args;
        return request();
    }

    private Response request() {
        return new Response(requestSpecification.request(call.method, call.path, args));
    }

    public class Response {
        io.restassured.response.Response response;

        public Response(io.restassured.response.Response response) {
            this.response = response;
        }

        public T asObject() {
            assertStatus();
            return response.as(Objects.requireNonNull(typeRef, "Не задан Class у ApiClient"));
        }

        public <R extends Page<?>> T asObjectList() {
            int i = 1;
            Page page = (Page) asObject();
            List items = page.getList();
            int count = items.size();
            if (Objects.nonNull(page.getMeta()))
                count = page.getMeta().getTotalCount();
            while (count > items.size()) {
                requestSpecification.queryParams("page", "" + (++i));
                page = (Page) request().asObject();
                items.addAll(page.getList());
            }
            page.setList(items);
            Assertions.assertEquals(count, items.size(), "Размер списка не равен TotalCount");
            return (T) page;
        }

        public io.restassured.response.Response response() {
            return response;
        }

        public Response assertStatus() {
            QueryableRequestSpecification request = SpecificationQuerier.query(requestSpecification);
            if (call.status != response.getStatusCode())
                throw new StatusResponseException(
                        call.status,
                        response.getStatusCode(),
                        request.getMethod(),
                        "-",
                        request.getHeaders().getValue("Authorization"),
                        response.getHeaders().toString(),
                        request.getURI(),
                        request.getBody(),
                        response.asPrettyString()
                );
            return this;
        }
    }
}
