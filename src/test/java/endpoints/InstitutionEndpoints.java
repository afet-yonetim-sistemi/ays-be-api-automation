package endpoints;

import io.restassured.response.Response;
import org.openqa.selenium.remote.http.HttpMethod;
import payload.*;

import java.util.Map;

public class InstitutionEndpoints {
    public static Response listAdmins(String pagination) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url(Routes.postAdmins)
                .body(pagination)
                .token(Authorization.institutionAuthorization())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response createAUser(User userPayload) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url(Routes.postCreateUser)
                .body(userPayload)
                .token(Authorization.institutionAuthorization())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response listUsers(RequestBodyUsers requestBodyUsers) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url(Routes.postUsers)
                .body(requestBodyUsers)
                .token(Authorization.institutionAuthorization())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response getUser(String userId) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.GET)
                .url(Routes.getUser)
                .pathParameter(Map.of("id", userId))
                .token(Authorization.institutionAuthorization())
                .build();

        return AysRestAssured.perform(restAssuredRequest);

    }

    public static Response updateUser(String userId, User userPayload) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.PUT)
                .url(Routes.putUpdateUser)
                .pathParameter(Map.of("id", userId))
                .body(userPayload)
                .token(Authorization.institutionAuthorization())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response deleteUser(String userId) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.DELETE)
                .url(Routes.deleteUser)
                .pathParameter(Map.of("id", userId))
                .token(Authorization.institutionAuthorization())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response listAssignments(RequestBodyAssignments bodyAssignments) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url(Routes.postListAssignments)
                .body(bodyAssignments)
                .token(Authorization.institutionAuthorization())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response createAnAssignment(Assignment assignment) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url(Routes.postCreateAssignment)
                .body(assignment)
                .token(Authorization.institutionAuthorization())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response getAssignment(String assignmentId) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.GET)
                .url(Routes.getAssignment)
                .pathParameter(Map.of("id", assignmentId))
                .token(Authorization.institutionAuthorization())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response updateAssignment(String assignmentId, Assignment assignment) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.PUT)
                .url(Routes.putUpdateAssignment)
                .pathParameter(Map.of("id", assignmentId))
                .body(assignment)
                .token(Authorization.institutionAuthorization())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response deleteAssignment(String assignmentId) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.DELETE)
                .url(Routes.deleteAssignment)
                .pathParameter(Map.of("id", assignmentId))
                .token(Authorization.institutionAuthorization())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response postRegistrationApplications(RequestBodyInstitution requestBodyInstitution) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url(Routes.postRegistrationApplications)
                .body(requestBodyInstitution)
                .token(Authorization.superAdminAuthorization())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }


    public static Response getRegistrationApplicationId(String applicationID) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.GET)
                .url(Routes.getRegistrationApplicationsWithId)
                .pathParameter(Map.of("id", applicationID))
                .token(Authorization.superAdminAuthorization())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }


    public static Response getRegistrationApplicationsIdSummary(String applicationID) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.GET)
                .url(Routes.getGetRegistrationApplicationsIdSummary)
                .pathParameter(Map.of("id", applicationID))
                .token(Authorization.institutionAuthorization())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response postRegistrationApplicationApprove(String applicationID) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url(Routes.postRegistrationApplicationApprove)
                .pathParameter(Map.of("id", applicationID))
                .token(Authorization.superAdminAuthorization())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response postRegistrationAdminApplication(ApplicationRegistration applicationRegistration) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url(Routes.postRegistrationApplication)
                .body(applicationRegistration)
                .token(Authorization.superAdminAuthorization())
                .build();

        return AysRestAssured.perform(restAssuredRequest);

    }

    public static Response postRegistrationApplicationReject(String applicationID, RejectReason rejectReason) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url(Routes.postRegistrationApplicationReject)
                .pathParameter(Map.of("id", applicationID))
                .body(rejectReason)
                .token(Authorization.superAdminAuthorization())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

}






