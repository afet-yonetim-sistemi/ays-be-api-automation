package endpoints;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.openqa.selenium.remote.http.HttpMethod;
import payload.Location;
import payload.Reason;

import static io.restassured.RestAssured.given;

public class UserEndpoints {
    public static Response updateSupportStatus(String payload, String username, String password) {
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.PUT)
                .url(Routes.putUpdateUserSupportStatus)
                .body(payload)
                .token(Authorization.userAuthorization(username, password))
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response updateLocation(Location location, String username, String password) {
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url(Routes.postUserLocation)
                .body(location)
                .token(Authorization.userAuthorization(username, password))
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response searchAssignment(Location location, String username, String password) {
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url(Routes.postAssignmentSearch)
                .body(location)
                .token(Authorization.userAuthorization(username, password))
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response approveAssignment(String username, String password) {
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url(Routes.postAssignmentApprove)
                .token(Authorization.userAuthorization(username, password))
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response rejectAssignment(String username, String password) {
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url(Routes.postAssignmentReject)
                .token(Authorization.userAuthorization(username, password))
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response startAssignment(String username, String password) {
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url(Routes.postAssignmentStart)
                .token(Authorization.userAuthorization(username, password))
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response completeAssignment(String username, String password) {
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url(Routes.postAssignmentComplete)
                .token(Authorization.userAuthorization(username, password))
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }
    public static Response getUserSelfInfo(String username, String password){
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.GET)
                .url(Routes.getUserSelfInfo)
                .token(Authorization.userAuthorization(username, password))
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }
    public static Response getAssignmentUser(String username, String password){
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.GET)
                .url(Routes.getAssignmentUser)
                .token(Authorization.userAuthorization(username, password))
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }
    public static Response getAssignmentSummaryUser(String username, String password){
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.GET)
                .url(Routes.getAssignmentSummaryUser)
                .token(Authorization.userAuthorization(username, password))
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }
    public static Response cancelAssignment(Reason reason, String username, String password) {
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url(Routes.postAssignmentCancel)
                .body(reason)
                .token(Authorization.userAuthorization(username, password))
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }



}
