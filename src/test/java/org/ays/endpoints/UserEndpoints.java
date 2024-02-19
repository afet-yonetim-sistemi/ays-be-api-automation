package org.ays.endpoints;

import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import org.ays.payload.Location;
import org.ays.payload.Reason;
import org.openqa.selenium.remote.http.HttpMethod;

@UtilityClass
public class UserEndpoints {
    public static Response updateSupportStatus(String payload, String username, String password) {
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.PUT)
                .url("/api/v1/user-self/status/support")
                .body(payload)
                .token(Authorization.loginAndGetUserAccessToken(username, password))
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response updateLocation(Location location, String username, String password) {
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/user/location")
                .body(location)
                .token(Authorization.loginAndGetUserAccessToken(username, password))
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response searchAssignment(Location location, String username, String password) {
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/assignment/search")
                .body(location)
                .token(Authorization.loginAndGetUserAccessToken(username, password))
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response approveAssignment(String username, String password) {
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/assignment/approve")
                .token(Authorization.loginAndGetUserAccessToken(username, password))
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response rejectAssignment(String username, String password) {
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/assignment/reject")
                .token(Authorization.loginAndGetUserAccessToken(username, password))
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response startAssignment(String username, String password) {
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/assignment/start")
                .token(Authorization.loginAndGetUserAccessToken(username, password))
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response completeAssignment(String username, String password) {
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/assignment/complete")
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

    public static Response getAssignmentUser(String username, String password) {
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.GET)
                .url("/api/v1/assignment")
                .token(Authorization.loginAndGetUserAccessToken(username, password))
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response getAssignmentSummaryUser(String username, String password) {
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.GET)
                .url("/api/v1/assignment/summary")
                .token(Authorization.loginAndGetUserAccessToken(username, password))
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response cancelAssignment(Reason reason, String username, String password) {
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/assignment/cancel")
                .body(reason)
                .token(Authorization.loginAndGetUserAccessToken(username, password))
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }


}
