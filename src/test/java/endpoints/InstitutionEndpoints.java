package endpoints;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import payload.RequestBodyAssignments;
import payload.User;

import static io.restassured.RestAssured.given;

public class InstitutionEndpoints extends AuthorizationTest {
    public static Response listAdmins(String pagination){
        return given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(pagination)
                .when()
                .post(Routes.postAdmins);
    }
    public static Response createAUser(User userPayload) {
        return given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(userPayload)
                .when()
                .post(Routes.postCreateUser);

    }

    public static Response listUsers(String pagination) {
        return given()
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(ContentType.JSON)
                        .body(pagination)
                        .when()
                        .post(Routes.postUsers);
    }

    public static Response getUser(String userId) {
        return given()
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("id", userId)
                .contentType(ContentType.JSON)
                .when()
                .get(Routes.getUser);

    }

    public static Response updateUser(String userId, User userPayload) {
        return given()
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("id", userId)
                .contentType(ContentType.JSON)
                .body(userPayload)
                .when()
                .put(Routes.putUpdateUser);
    }

    public static Response deleteUser(String userId) {
        return given()
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("id", userId)
                .contentType(ContentType.JSON)
                .when()
                .delete(Routes.deleteUser);
    }
    public static Response listAssignments(RequestBodyAssignments bodyAssignments){
        return given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(bodyAssignments)
                .when()
                .post(Routes.postListAssignments);
    }


}
