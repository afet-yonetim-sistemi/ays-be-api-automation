package org.ays.endpoints;

import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import org.ays.payload.UserSupportStatusUpdatePayload;
import org.openqa.selenium.remote.http.HttpMethod;

@UtilityClass
public class UserEndpoints {

    public static Response updateSupportStatus(UserSupportStatusUpdatePayload payload, String username, String password) {
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.PUT)
                .url("/api/v1/user-self/status/support")
                .body(payload)
                .token(Authorization.loginAndGetUserAccessToken(username, password))
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response getUserSelfInfo(String username, String password) {
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.GET)
                .url("/api/v1/user-self")
                .token(Authorization.loginAndGetUserAccessToken(username, password))
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }


}
