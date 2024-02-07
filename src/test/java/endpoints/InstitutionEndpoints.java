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

//        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
//                .httpMethod(HttpMethod.POST)
//                .url(Routes.postAdmins)
//                .body(pagination)
//                .build();
//
//        return AysRestAssured.perform(restAssuredRequest);
        return given()
                .header("Authorization", "Bearer " + Authorization.institutionAuthorization())
                .contentType(ContentType.JSON)
                .body(pagination)
                .when()
                .post(Routes.postAdmins);
    }

    public static Response createAUser(User userPayload) {

//        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
//                .httpMethod(HttpMethod.POST)
//                .url(Routes.postCreateUser)
//                .body(userPayload)
//                .build();
//
//        return AysRestAssured.perform(restAssuredRequest);
        return given()
                .header("Authorization", "Bearer " + Authorization.institutionAuthorization())
                .contentType(ContentType.JSON)
                .body(userPayload)
                .when()
                .post(Routes.postCreateUser);

    }

    public static Response listUsers(RequestBodyUsers requestBodyUsers) {

//        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
//                .httpMethod(HttpMethod.POST)
//                .url(Routes.postUsers)
//                .body(requestBodyUsers)
//                .build();
//
//        return AysRestAssured.perform(restAssuredRequest);
        return given()
                .header("Authorization", "Bearer " + Authorization.institutionAuthorization())
                .contentType(ContentType.JSON)
                .body(requestBodyUsers)
                .when()
                .post(Routes.postUsers);
    }

    public static Response getUser(String userId) {

//        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
//                .httpMethod(HttpMethod.GET)
//                .url(Routes.getUser)
//                .pathParameter(Map.of("id", userId))
//                .build();
//
//        return AysRestAssured.perform(restAssuredRequest);
        return given()
                .header("Authorization", "Bearer " + Authorization.institutionAuthorization())
                .pathParam("id", userId)
                .contentType(ContentType.JSON)
                .when()
                .get(Routes.getUser);

    }

    public static Response updateUser(String userId, User userPayload) {

//        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
//                .httpMethod(HttpMethod.PUT)
//                .url(Routes.putUpdateUser)
//                .pathParameter(Map.of("id",userId))
//                .body(userPayload)
//                .build();
//
//        return AysRestAssured.perform(restAssuredRequest);
        return given()
                .header("Authorization", "Bearer " + Authorization.institutionAuthorization())
                .pathParam("id", userId)
                .contentType(ContentType.JSON)
                .body(userPayload)
                .when()
                .put(Routes.putUpdateUser);
    }

    public static Response deleteUser(String userId) {

//        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
//                .httpMethod(HttpMethod.DELETE)
//                .url(Routes.deleteUser)
//                .pathParameter(Map.of("id",userId))
//                .build();
//
//        return AysRestAssured.perform(restAssuredRequest);
        return given()
                .header("Authorization", "Bearer " + Authorization.institutionAuthorization())
                .pathParam("id", userId)
                .contentType(ContentType.JSON)
                .when()
                .delete(Routes.deleteUser);
    }

    public static Response listAssignments(RequestBodyAssignments bodyAssignments) {

//        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
//                .httpMethod(HttpMethod.POST)
//                .url(Routes.postListAssignments)
//                .body(bodyAssignments)
//                .build();
//
//        return AysRestAssured.perform(restAssuredRequest);
        return given()
                .header("Authorization", "Bearer " + Authorization.institutionAuthorization())
                .contentType(ContentType.JSON)
                .body(bodyAssignments)
                .when()
                .post(Routes.postListAssignments);
    }

    public static Response createAnAssignment(Assignment assignment) {

//        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
//                .httpMethod(HttpMethod.POST)
//                .url(Routes.postCreateAssignment)
//                .body(assignment)
//                .build();
//
//        return AysRestAssured.perform(restAssuredRequest);
        return given()
                .header("Authorization", "Bearer " + Authorization.institutionAuthorization())
                .contentType(ContentType.JSON)
                .body(assignment)
                .when()
                .post(Routes.postCreateAssignment);
    }

    public static Response getAssignment(String assignmentId) {

//        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
//                .httpMethod(HttpMethod.GET)
//                .url(Routes.getAssignment)
//                .pathParameter(Map.of("id", assignmentId))
//                .build();
//
//        return AysRestAssured.perform(restAssuredRequest);
        return given()
                .header("Authorization", "Bearer " + Authorization.institutionAuthorization())
                .pathParam("id", assignmentId)
                .contentType(ContentType.JSON)
                .when()
                .get(Routes.getAssignment);

    }

    public static Response updateAssignment(String assignmentId, Assignment assignment) {

//        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
//                .httpMethod(HttpMethod.PUT)
//                .url(Routes.putUpdateAssignment)
//                .pathParameter(Map.of("id", assignmentId))
//                .body(assignment)
//                .build();
//
//        return AysRestAssured.perform(restAssuredRequest);
        return given()
                .header("Authorization", "Bearer " + Authorization.institutionAuthorization())
                .pathParam("id", assignmentId)
                .contentType(ContentType.JSON)
                .body(assignment)
                .when()
                .put(Routes.putUpdateAssignment);
    }

    public static Response deleteAssignment(String assignmentId) {

//        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
//                .httpMethod(HttpMethod.DELETE)
//                .url(Routes.deleteAssignment)
//                .pathParameter(Map.of("id", assignmentId))
//                .build();
//
//        return AysRestAssured.perform(restAssuredRequest);
        return given()
                .header("Authorization", "Bearer " + Authorization.institutionAuthorization())
                .pathParam("id", assignmentId)
                .contentType(ContentType.JSON)
                .when()
                .delete(Routes.deleteAssignment);
    }

    public static Response postRegistrationApplications(RequestBodyInstitution requestBodyInstitution) {

//        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
//                .httpMethod(HttpMethod.POST)
//                .url(Routes.postRegistrationApplications)
//                .body(requestBodyInstitution)
//                .build();
//
//        return AysRestAssured.perform(restAssuredRequest);
            return given()
                    .header("Authorization", "Bearer " + Authorization.superAdminAuthorization())
                    .contentType(ContentType.JSON)
                    .body(requestBodyInstitution)
                    .when()
                    .post(Routes.postRegistrationApplications);
        }


    public static Response getRegistrationApplicationId(String applicationID) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.GET)
                .url(Routes.getRegistrationApplicationsWithId)
                .pathParameter(Map.of("id", applicationID))
                .build();

        return AysRestAssured.perform(restAssuredRequest);
//        return given()
//                .header("Authorization", "Bearer " + Authorization.superAdminAuthorization())
//                .pathParam("id", applicationID)
//                .contentType(ContentType.JSON)
//                .when()
//                .get(Routes.getRegistrationApplicationsWithId);
    }


    public static Response getRegistrationApplicationsIdSummary(String applicationID) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.GET)
                .url(Routes.getGetRegistrationApplicationsIdSummary)
                .pathParameter(Map.of("id", applicationID))
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response postRegistrationApplicationApprove(String applicationID) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url(Routes.postRegistrationApplicationApprove)
                .pathParameter(Map.of("id", applicationID))
                .build();

        return AysRestAssured.perform(restAssuredRequest);
//        return given()
//                .header("Authorization", "Bearer " + Authorization.superAdminAuthorization())
//                .pathParam("id", applicationID)
//                .contentType(ContentType.JSON)
//                .when()
//                .post(Routes.postRegistrationApplicationApprove);
    }
    public static Response postRegistrationAdminApplication(ApplicationRegistration applicationRegistration){

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url(Routes.postRegistrationApplication)
                .body(applicationRegistration)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
//        return given()
//                .header("Authorization", "Bearer " + Authorization.superAdminAuthorization())
//                .contentType(ContentType.JSON)
//                .body(applicationRegistration)
//                .when()
//                .post(Routes.postRegistrationApplication);

    }
    public static Response postRegistrationApplicationReject(String applicationID,RejectReason rejectReason) {

//        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
//                .httpMethod(HttpMethod.POST)
//                .url(Routes.postRegistrationApplicationReject)
//                .pathParameter(Map.of("id", applicationID))
//                .body(rejectReason)
//                .build();
//
//        return AysRestAssured.perform(restAssuredRequest);
        return given()
                .header("Authorization", "Bearer " + Authorization.superAdminAuthorization())
                .pathParam("id", applicationID)
                .contentType(ContentType.JSON)
                .body(rejectReason)
                .when()
                .post(Routes.postRegistrationApplicationReject);
    }

}






