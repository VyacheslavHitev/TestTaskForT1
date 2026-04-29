package core.helper.http;

import lombok.Getter;

@Getter
public class StatusResponseException extends AssertionError {
    private int expectStatus;
    private int status;
    private String method;
    private String role;
    private String token;
    private String headers;
    private String url;
    private String body;
    private String responseMessage;

    public StatusResponseException(String errorMessage) {
        super(errorMessage);
    }

    public StatusResponseException(int expectStatus, int status, String method, String role, String token, String headers, String url, String body, String responseMessage) {
        super("""
                                
                expected:<%d>
                but was:<%d>
                               
                Method: %s
                Role: %s
                Token: %s
                Headers:
                %s
                Url: %s
                Request: %s
                Response: %s
                """.formatted(expectStatus, status, method, role, token, headers, url, body, responseMessage));
        this.expectStatus = expectStatus;
        this.status = status;
        this.method = method;
        this.role = role;
        this.token = token;
        this.headers = headers;
        this.url = url;
        this.body = body;
        this.responseMessage = responseMessage;
    }

    public StatusResponseException(Throwable throwable, String method, String token, String headers, String url, String body, String responseMessage) {
        super("""   
                %s
                Method: %s
                Token: %s
                Headers:
                %s
                Url: %s
                Body: %s
                Response: %s
                """.formatted(throwable.getMessage(), method, token, headers, url, body, responseMessage));
        setStackTrace(throwable.getStackTrace());
    }
}
