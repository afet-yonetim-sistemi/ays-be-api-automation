package endpoints;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import payload.Location;

import static io.restassured.RestAssured.given;


public class UserEndpoints {
    public static Response updateSupportStatus(String payload, String username, String password) {
        return given()
                .header("Authorization", "Bearer " + Authorization.userAuthorization(username, password))
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .put(Routes.putUpdateUserSupportStatus);
    }

    public static Response updateLocation(Location location, String username, String password) {
        return given()
                .header("Authorization", "Bearer " + Authorization.userAuthorization(username, password))
                .contentType(ContentType.JSON)
                .body(location)
                .when()
                .post(Routes.postUserLocation);
    }

    public static Response searchAssignment(Location location, String username, String password) {
        return given()
                .header("Authorization", "Bearer " + Authorization.userAuthorization(username, password))
                .contentType(ContentType.JSON)
                .body(location)
                .when()
                .post(Routes.postAssignmentSearch);
    }

    public static Response approveAssignment(String username, String password) {
        return given()
                .header("Authorization", "Bearer " + Authorization.userAuthorization(username, password))
                .contentType(ContentType.JSON)
                .when()
                .post(Routes.postAssignmentApprove);
    }

    public static Response rejectAssignment(String username, String password) {
        return given()
                .header("Authorization", "Bearer " + Authorization.userAuthorization(username, password))
                .contentType(ContentType.JSON)
                .when()
                .post(Routes.postAssignmentReject);
    }

    public static Response startAssignment(String username, String password) {
        return given()
                .header("Authorization", "Bearer " + Authorization.userAuthorization(username, password))
                .contentType(ContentType.JSON)
                .when()
                .post(Routes.postAssignmentStart);
    }

    public static Response completeAssignment(String username, String password) {
        return given()
                .header("Authorization", "Bearer " + Authorization.userAuthorization(username, password))
                .contentType(ContentType.JSON)
                .when()
                .post(Routes.postAssignmentComplete);
    }


}
