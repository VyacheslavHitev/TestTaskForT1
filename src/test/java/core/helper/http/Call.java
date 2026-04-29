package core.helper.http;

import io.restassured.http.Method;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Call {
    Method method;
    String path;
    int status;
    String url;
}
