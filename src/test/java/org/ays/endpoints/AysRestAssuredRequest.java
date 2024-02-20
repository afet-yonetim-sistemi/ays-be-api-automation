package org.ays.endpoints;

import io.restassured.http.ContentType;
import lombok.Builder;
import lombok.Getter;
import org.openqa.selenium.remote.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
public class AysRestAssuredRequest {

    private HttpMethod httpMethod;
    private String url;
    private Object body;
    private String token;
    @Builder.Default
    private Map<String, Object> pathParameter = new HashMap<>();
    @Builder.Default
    private ContentType contentType = ContentType.JSON;

    public boolean isTokenExist() {
        return this.token != null;
    }

    public boolean isRequestBodyExist() {
        return this.body != null;
    }

}
