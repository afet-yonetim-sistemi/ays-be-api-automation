package endpoints;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import payload.User;

import static io.restassured.RestAssured.given;


public class UserEndpoints extends UserAuthorizationTest {
    public static Response updateSupportStatus(String payload){
        return given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .put(Routes.putUpdateUserSupportStatus);
    }
    public static Response updateLocation(String payload){
        return given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post(Routes.postUserLocation);
    }
    public static Response searchAssignment(String payload){
        return given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post(Routes.postAssignmentSearch);
    }
    public static Response approveAssignment(){
        return given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .when()
                .post(Routes.postAssignmentApprove);
    }
    public static Response rejectAssignment(){
        return given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .when()
                .post(Routes.postAssignmentReject);
    }
    public static Response startAssignment(){
        return given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .when()
                .post(Routes.postAssignmentStart);
    }
    public static Response completeAssignment(){
        return given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .when()
                .post(Routes.postAssignmentComplete);
    }


}
