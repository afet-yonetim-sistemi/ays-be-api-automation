package org.ays.auth.endpoints;

import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import org.ays.auth.datasource.UserDataSource;
import org.ays.auth.payload.LoginPayload;
import org.ays.auth.payload.UserCreatePayload;
import org.ays.auth.payload.UserListPayload;
import org.ays.auth.payload.UserUpdatePayload;
import org.ays.common.model.payload.AysRestAssuredPayload;
import org.ays.common.util.AysConfigurationProperty;
import org.ays.common.util.AysRestAssured;
import org.openqa.selenium.remote.http.HttpMethod;

import java.util.Map;

@UtilityClass
public class UserEndpoints {

    public static Response listUsers(UserListPayload userListPayload, String token) {

        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/users")
                .body(userListPayload)
                .token(token)
                .build();

        return AysRestAssured.perform(restAssuredPayload);
    }

    public static Response getUser(String userId, String token) {

        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.GET)
                .url("/api/v1/user/{id}")
                .pathParameter(Map.of("id", userId))
                .token(token)
                .build();

        return AysRestAssured.perform(restAssuredPayload);
    }

    public static Response createAUser(UserCreatePayload userCreatePayloadPayload, String token) {

        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/user")
                .body(userCreatePayloadPayload)
                .token(token)
                .build();

        return AysRestAssured.perform(restAssuredPayload);
    }

    public static Response updateUser(String id, UserUpdatePayload updatePayload, String token) {

        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.PUT)
                .url("/api/v1/user/{id}")
                .pathParameter(Map.of("id", id))
                .body(updatePayload)
                .token(token)
                .build();

        return AysRestAssured.perform(restAssuredPayload);
    }

    public static Response deleteUser(String userId, String token) {

        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.DELETE)
                .url("/api/v1/user/{id}")
                .pathParameter(Map.of("id", userId))
                .token(token)
                .build();

        return AysRestAssured.perform(restAssuredPayload);
    }

    public static String generateUserId(UserCreatePayload userCreatePayload) {

        LoginPayload loginPayload = LoginPayload.generateAsTestAdmin();
        String accessToken = AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");

        Response response = createAUser(userCreatePayload, accessToken);

        if (response.getStatusCode() == 200) {
            return UserDataSource.findLastCreatedUserIdByInstitutionId(AysConfigurationProperty.Database.TEST_FOUNDATION_ID);
        } else {
            throw new RuntimeException("Role creation failed with status code: " + response.getStatusCode());
        }
    }

}
