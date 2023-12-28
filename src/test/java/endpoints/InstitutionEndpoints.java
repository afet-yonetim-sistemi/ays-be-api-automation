package endpoints;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import payload.*;

import static io.restassured.RestAssured.given;

public class InstitutionEndpoints {
    public static Response listAdmins(String pagination) {
        return given()
                .header("Authorization", "Bearer " + Authorization.institutionAuthorization())
                .contentType(ContentType.JSON)
                .body(pagination)
                .when()
                .post(Routes.postAdmins);
    }

    public static Response createAUser(User userPayload) {
        return given()
                .header("Authorization", "Bearer " + Authorization.institutionAuthorization())
                .contentType(ContentType.JSON)
                .body(userPayload)
                .when()
                .post(Routes.postCreateUser);

    }

    public static Response listUsers(RequestBodyAssignments requestBodyAssignments) {
        return given()
                .header("Authorization", "Bearer " + Authorization.institutionAuthorization())
                .contentType(ContentType.JSON)
                .body(requestBodyAssignments)
                .when()
                .post(Routes.postUsers);
    }

    public static Response getUser(String userId) {
        return given()
                .header("Authorization", "Bearer " + Authorization.institutionAuthorization())
                .pathParam("id", userId)
                .contentType(ContentType.JSON)
                .when()
                .get(Routes.getUser);

    }

    public static Response updateUser(String userId, User userPayload) {
        return given()
                .header("Authorization", "Bearer " + Authorization.institutionAuthorization())
                .pathParam("id", userId)
                .contentType(ContentType.JSON)
                .body(userPayload)
                .when()
                .put(Routes.putUpdateUser);
    }

    public static Response deleteUser(String userId) {
        return given()
                .header("Authorization", "Bearer " + Authorization.institutionAuthorization())
                .pathParam("id", userId)
                .contentType(ContentType.JSON)
                .when()
                .delete(Routes.deleteUser);
    }

    public static Response listAssignments(RequestBodyAssignments bodyAssignments) {
        return given()
                .header("Authorization", "Bearer " + Authorization.institutionAuthorization())
                .contentType(ContentType.JSON)
                .body(bodyAssignments)
                .when()
                .post(Routes.postListAssignments);
    }

    public static Response createAnAssignment(Assignment assignment) {
        return given()
                .header("Authorization", "Bearer " + Authorization.institutionAuthorization())
                .contentType(ContentType.JSON)
                .body(assignment)
                .when()
                .post(Routes.postCreateAssignment);
    }
    public static Response createAnInstitution(Institution institution) {
        return given()
                .header("Authorization", "Bearer " + Authorization.superAdminAuthorization())
                .contentType(ContentType.JSON)
                .body(institution)
                .when()
                .post(Routes.postRegistrationApplications);
    }

    public static Response getAssignment(String assignmentId) {
        return given()
                .header("Authorization", "Bearer " + Authorization.institutionAuthorization())
                .pathParam("id", assignmentId)
                .contentType(ContentType.JSON)
                .when()
                .get(Routes.getAssignment);

    }

    public static Response updateAssignment(String assignmentId, Assignment assignment) {
        return given()
                .header("Authorization", "Bearer " + Authorization.institutionAuthorization())
                .pathParam("id", assignmentId)
                .contentType(ContentType.JSON)
                .body(assignment)
                .when()
                .put(Routes.putUpdateAssignment);
    }

    public static Response deleteAssignment(String assignmentId) {
        return given()
                .header("Authorization", "Bearer " + Authorization.institutionAuthorization())
                .pathParam("id", assignmentId)
                .contentType(ContentType.JSON)
                .when()
                .delete(Routes.deleteAssignment);
    }

    public static Response postRegistrationApplications(RequestBodyInstitution requestBodyInstitution, String userType) {
        if (userType.equals("SUPER_ADMIN")) {
            return given()
                    .header("Authorization", "Bearer " + Authorization.superAdminAuthorization())
                    .contentType(ContentType.JSON)
                    .body(requestBodyInstitution)
                    .when()
                    .post(Routes.postRegistrationApplications);
        }
        return given()
                .header("Authorization", "Bearer " + Authorization.institutionAuthorization())
                .contentType(ContentType.JSON)
                .body(requestBodyInstitution)
                .when()
                .post(Routes.postRegistrationApplications);

    }

    public static Response getRegistrationApplicationsId(String institutionID) {

        return given()
                .header("Authorization", "Bearer " + Authorization.superAdminAuthorization())
                .pathParam("id", institutionID)
                .contentType(ContentType.JSON)
                .when()
                .get(Routes.getRegistrationApplicationsWithId);
    }
}






