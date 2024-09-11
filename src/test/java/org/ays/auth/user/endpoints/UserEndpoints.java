package org.ays.auth.user.endpoints;

import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import org.ays.common.model.payload.AysRestAssuredPayload;
import org.ays.common.util.AysRestAssured;
import org.ays.endpoints.Authorization;
import org.ays.payload.AdminCredentials;
import org.ays.payload.RequestBodyUsers;
import org.ays.payload.User;
import org.openqa.selenium.remote.http.HttpMethod;

import java.util.Map;

@UtilityClass
public class UserEndpoints {

    public static Response createAUser(User userPayload) {

        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/user")
                .body(userPayload)
                .token(Authorization.loginAndGetAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response createAUser(User userPayload, String token) {

        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/user")
                .body(userPayload)
                .token(token)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response listUsers(RequestBodyUsers requestBodyUsers, AdminCredentials adminCredentials) {

        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/users")
                .body(requestBodyUsers)
                .token(Authorization.loginAndGetAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response listUsersTwo(RequestBodyUsers requestBodyUsers) {

        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/users")
                .body(requestBodyUsers)
                .token(Authorization.loginAndGetAdminTwoAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response listUsersSuperAdmin(RequestBodyUsers requestBodyUsers) {

        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/users")
                .body(requestBodyUsers)
                .token(Authorization.loginAndGetSuperAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response getUser(String userId) {

        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.GET)
                .url("/api/v1/user/{id}")
                .pathParameter(Map.of("id", userId))
                .token(Authorization.loginAndGetAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);

    }

    public static Response deleteUser(String userId) {

        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.DELETE)
                .url("/api/v1/user/{id}")
                .pathParameter(Map.of("id", userId))
                .token(Authorization.loginAndGetAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

}
