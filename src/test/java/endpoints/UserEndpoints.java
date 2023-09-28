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
                .put(Routes.postUserLocation);
    }
    public static Response searchAssignment(String payload){
        return given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .put(Routes.postAssignmentSearch);
    }


}
