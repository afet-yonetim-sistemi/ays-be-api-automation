package org.ays.auth.endpoints;

import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import org.ays.auth.payload.UserCreatePayload;
import org.ays.auth.payload.UserListPayload;
import org.ays.auth.payload.UserUpdatePayload;
import org.ays.common.model.payload.AysRestAssuredPayload;
import org.ays.common.util.AysRestAssured;
import org.openqa.selenium.remote.http.HttpMethod;

import java.util.Map;

@UtilityClass
public class UserEndpoints {

    public static Response findAll(UserListPayload userListPayload, String token) {

        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/users")
                .body(userListPayload)
                .token(token)
                .build();

        return AysRestAssured.perform(restAssuredPayload);
    }

    public static Response findById(String id, String token) {

        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.GET)
                .url("/api/v1/user/{id}")
                .pathParameter(Map.of("id", id))
                .token(token)
                .build();

        return AysRestAssured.perform(restAssuredPayload);
    }

    public static Response create(UserCreatePayload userCreatePayloadPayload, String token) {

        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/user")
                .body(userCreatePayloadPayload)
                .token(token)
                .build();

        return AysRestAssured.perform(restAssuredPayload);
    }

    public static Response update(String id, UserUpdatePayload updatePayload, String token) {

        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.PUT)
                .url("/api/v1/user/{id}")
                .pathParameter(Map.of("id", id))
                .body(updatePayload)
                .token(token)
                .build();

        return AysRestAssured.perform(restAssuredPayload);
    }

    public static Response passivate(String userId, String token) {

        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.PATCH)
                .url("/api/v1/user/{id}/passivate")
                .pathParameter(Map.of("id", userId))
                .token(token)
                .build();

        return AysRestAssured.perform(restAssuredPayload);
    }

    public static Response activate(String userId, String token) {

        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.PATCH)
                .url("/api/v1/user/{id}/activate")
                .pathParameter(Map.of("id", userId))
                .token(token)
                .build();

        return AysRestAssured.perform(restAssuredPayload);
    }

    public static Response delete(String userId, String token) {

        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.DELETE)
                .url("/api/v1/user/{id}")
                .pathParameter(Map.of("id", userId))
                .token(token)
                .build();

        return AysRestAssured.perform(restAssuredPayload);
    }
}
