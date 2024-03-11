package org.ays.endpoints;

import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import org.ays.payload.AdminsPayload;
import org.ays.payload.ApplicationRegistration;
import org.ays.payload.Assignment;
import org.ays.payload.RejectReason;
import org.ays.payload.RequestBodyAssignments;
import org.ays.payload.RequestBodyInstitution;
import org.ays.payload.RequestBodyUsers;
import org.ays.payload.User;
import org.ays.payload.UserCredentials;
import org.ays.utility.AysConfigurationProperty;
import org.ays.utility.AysRandomUtil;
import org.openqa.selenium.remote.http.HttpMethod;

import java.util.Map;

@UtilityClass
public class InstitutionEndpoints {

    public static Response listAdmins(AdminsPayload adminsPayload) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/admins")
                .body(adminsPayload)
                .token(Authorization.loginAndGetAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response createAUser(User userPayload) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/user")
                .body(userPayload)
                .token(Authorization.loginAndGetAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response listUsers(RequestBodyUsers requestBodyUsers) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/users")
                .body(requestBodyUsers)
                .token(Authorization.loginAndGetAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response getUser(String userId) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.GET)
                .url("/api/v1/user/{id}")
                .pathParameter(Map.of("id", userId))
                .token(Authorization.loginAndGetAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);

    }

    public static Response updateUser(String userId, User userPayload) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.PUT)
                .url("/api/v1/user/{id}")
                .pathParameter(Map.of("id", userId))
                .body(userPayload)
                .token(Authorization.loginAndGetAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response deleteUser(String userId) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.DELETE)
                .url("/api/v1/user/{id}")
                .pathParameter(Map.of("id", userId))
                .token(Authorization.loginAndGetAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response listAssignments(RequestBodyAssignments bodyAssignments) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/assignments")
                .body(bodyAssignments)
                .token(Authorization.loginAndGetAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response createAnAssignment(Assignment assignment) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/assignment")
                .body(assignment)
                .token(Authorization.loginAndGetAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response getAssignment(String assignmentId) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.GET)
                .url("/api/v1/assignment/{id}")
                .pathParameter(Map.of("id", assignmentId))
                .token(Authorization.loginAndGetAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response updateAssignment(String assignmentId, Assignment assignment) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.PUT)
                .url("/api/v1/assignment/{id}")
                .pathParameter(Map.of("id", assignmentId))
                .body(assignment)
                .token(Authorization.loginAndGetAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response deleteAssignment(String assignmentId) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.DELETE)
                .url("/api/v1/assignment/{id}")
                .pathParameter(Map.of("id", assignmentId))
                .token(Authorization.loginAndGetAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response postRegistrationApplications(RequestBodyInstitution requestBodyInstitution) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/admin/registration-applications")
                .body(requestBodyInstitution)
                .token(Authorization.loginAndGetSuperAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }


    public static Response getRegistrationApplicationId(String applicationID) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.GET)
                .url("/api/v1/admin/registration-application/{id}")
                .pathParameter(Map.of("id", applicationID))
                .token(Authorization.loginAndGetSuperAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }


    public static Response getRegistrationApplicationsIdSummary(String applicationID) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.GET)
                .url("/api/v1/admin/registration-application/{id}/summary")
                .pathParameter(Map.of("id", applicationID))
                .token(Authorization.loginAndGetAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response postRegistrationApplicationApprove(String applicationID) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/admin/registration-application/{id}/approve")
                .pathParameter(Map.of("id", applicationID))
                .token(Authorization.loginAndGetSuperAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response postRegistrationAdminApplication(ApplicationRegistration applicationRegistration) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/admin/registration-application")
                .body(applicationRegistration)
                .token(Authorization.loginAndGetSuperAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);

    }

    public static Response postRegistrationApplicationReject(String applicationID, RejectReason rejectReason) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/admin/registration-application/{id}/reject")
                .pathParameter(Map.of("id", applicationID))
                .body(rejectReason)
                .token(Authorization.loginAndGetSuperAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response getInstitutionsSummary() {
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.GET)
                .url("/api/v1/institutions/summary")
                .token(Authorization.loginAndGetSuperAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static String generateApplicationID() {
        ApplicationRegistration applicationRegistration = ApplicationRegistration.generate(AysConfigurationProperty.InstitutionOne.ID, AysRandomUtil.generateReasonString());
        Response response = postRegistrationAdminApplication(applicationRegistration);
        if (response.getStatusCode() == 200) {
            return response.then().extract().jsonPath().getString("response.id");
        } else {
            throw new RuntimeException("Application Id creation failed with status code: " + response.getStatusCode());
        }
    }

    public static UserCredentials generateANewUser() {
        User user = User.generate();
        Response response = createAUser(user);
        if (response.getStatusCode() == 200) {
            return response.then()
                    .extract().jsonPath().getObject("response", UserCredentials.class);
        } else {
            throw new RuntimeException("User creation failed with status code: " + response.getStatusCode());
        }
    }

    public static UserCredentials generateANewUser(User userPayload) {
        Response response = createAUser(userPayload);
        if (response.getStatusCode() == 200) {
            return response.then()
                    .extract().jsonPath().getObject("response", UserCredentials.class);
        } else {
            throw new RuntimeException("User creation failed with status code: " + response.getStatusCode());
        }
    }

    public static Assignment generateANewAssignment() {
        Assignment assignment = Assignment.generate();
        Response response = createAnAssignment(assignment);
        if (response.getStatusCode() == 200) {
            return assignment;
        } else {
            throw new RuntimeException("Assignment creation failed with status code: " + response.getStatusCode());
        }
    }

}






