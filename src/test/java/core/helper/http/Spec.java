package core.helper.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.specification.RequestSpecification;
import lombok.extern.log4j.Log4j2;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import static io.restassured.http.ContentType.JSON;

public class Spec {

    private static class CustomHttpClientFactory implements HttpClientConfig.HttpClientFactory {
        @Override
        @SuppressWarnings("deprecation")
        public HttpClient createHttpClient() {
            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, 120000);
            HttpConnectionParams.setSoTimeout(params, 120000);
            PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager();
            connectionManager.setMaxTotal(3);
            DefaultHttpClient client = new DefaultHttpClient(connectionManager, params);
            client.setHttpRequestRetryHandler((exception, executionCount, context) -> executionCount < 5);
            return client;
        }
    }

    @Log4j2
    public static class Log4JFilter implements Filter {
        @Override
        public Response filter(FilterableRequestSpecification req, FilterableResponseSpecification res, FilterContext ctx) {
            Response response = ctx.next(req, res);
            String requestBody = req.getBody() != null ? req.getBody().toString() : "";
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String prettyRequestBody = "";
            if (!requestBody.isEmpty()) {
                JsonElement jsonElement = gson.fromJson(requestBody, JsonElement.class);
                prettyRequestBody = gson.toJson(jsonElement);
            }
            log.debug("""
                    %s URL: %s
                    REQUEST %s
                    
                    RESPONSE (%s) (%s): %s
                    """.formatted(req.getMethod(), req.getURI(), prettyRequestBody, response.getStatusCode(),
                    response.getHeader("x-request-id"), response.asPrettyString()));
            return response;
        }
    }


    public static RequestSpecification requestSpecificationMain() {
        return RestAssured.with().spec(cloudBaseRequestSpecification()).filters(new Log4JFilter());
    }

    public static RequestSpecification cloudBaseRequestSpecification() {
        return RestAssured.with()
                .config(RestAssured.config().httpClient(HttpClientConfig.httpClientConfig().httpClientFactory(new CustomHttpClientFactory())))
                .header("User-Agent", "Apache-HttpClient/4.5.13 (Java/17.0.10)")
                .spec(new RequestSpecBuilder()
                        .setRelaxedHTTPSValidation()
                        .setBaseUri("https://ya.ru/")
                        .setContentType(JSON)
                        .setAccept(JSON)
                        .build());
    }
}