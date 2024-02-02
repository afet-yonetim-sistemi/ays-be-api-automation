package endpoints;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.openqa.selenium.remote.http.HttpMethod;
import payload.ApplicationRegistration;
import payload.Assignment;
import payload.RejectReason;
import payload.RequestBodyAssignments;
import payload.RequestBodyInstitution;
import payload.RequestBodyUsers;
import payload.User;

import java.util.Map;

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

    public static Response listUsers(RequestBodyUsers requestBodyUsers) {
        return given()
                .header("Authorization", "Bearer " + Authorization.institutionAuthorization())
                .contentType(ContentType.JSON)
                .body(requestBodyUsers)
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

    public static Response getRegistrationApplicationsId(String adminID) {

        return given()
                .header("Authorization", "Bearer " + Authorization.superAdminAuthorization())
                .pathParam("id", adminID)
                .contentType(ContentType.JSON)
                .when()
                .get(Routes.getRegistrationApplicationsWithId);
    }

    public static Response getRegistrationApplicationsIdSummary(String adminID) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.GET)
                .url(Routes.getGetRegistrationApplicationsIdSummary)
                .pathParameter(Map.of("id", adminID))
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response postRegistrationApplicationApprove(String applicationID) {
        return given()
                .header("Authorization", "Bearer " + Authorization.superAdminAuthorization())
                .pathParam("id", applicationID)
                .contentType(ContentType.JSON)
                .when()
                .post(Routes.postRegistrationApplicationApprove);
    }
    public static Response postRegistrationAdminApplication(ApplicationRegistration applicationRegistration){
        return given()
                .header("Authorization", "Bearer " + Authorization.superAdminAuthorization())
                .contentType(ContentType.JSON)
                .body(applicationRegistration)
                .when()
                .post(Routes.postRegistrationApplication);

    }
    public static Response postRegistrationApplicationReject(String applicationID,RejectReason rejectReason) {
        return given()
                .header("Authorization", "Bearer " + Authorization.superAdminAuthorization())
                .pathParam("id", applicationID)
                .contentType(ContentType.JSON)
                .body(rejectReason)
                .when()
                .post(Routes.postRegistrationApplicationReject);
    }

}






