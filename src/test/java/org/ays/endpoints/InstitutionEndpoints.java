package org.ays.endpoints;

import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import org.ays.common.configuration.AysRestAssured;
import org.ays.common.model.request.AysRestAssuredRequest;
import org.ays.payload.AdminCredentials;
import org.ays.payload.AdminsListPayload;
import org.ays.payload.ApplicationRegistration;
import org.ays.payload.ApplicationRegistrationSupportStatus;
import org.ays.payload.Filter;
import org.ays.payload.ListEmergencyEvacuationApplications;
import org.ays.payload.Pageable;
import org.ays.payload.RegistrationApplicationCompletePayload;
import org.ays.payload.RejectReason;
import org.ays.payload.RequestBodyInstitution;
import org.ays.payload.RequestBodyUsers;
import org.ays.payload.RoleCreatePayload;
import org.ays.payload.RoleUpdatePayload;
import org.ays.payload.RolesListPayload;
import org.ays.payload.User;
import org.ays.payload.UserCredentials;
import org.ays.utility.AysConfigurationProperty;
import org.ays.utility.AysRandomUtil;
import org.ays.utility.DatabaseUtility;
import org.openqa.selenium.remote.http.HttpMethod;

import java.util.Map;

@UtilityClass
public class InstitutionEndpoints {

    public static Response listAdmins(AdminsListPayload adminsListPayload) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/admins")
                .body(adminsListPayload)
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

    public static Response createAUser(User userPayload, String token) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/user")
                .body(userPayload)
                .token(token)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response listUsers(RequestBodyUsers requestBodyUsers, AdminCredentials adminCredentials) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/users")
                .body(requestBodyUsers)
                .token(Authorization.loginAndGetAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response listUsersTwo(RequestBodyUsers requestBodyUsers) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/users")
                .body(requestBodyUsers)
                .token(Authorization.loginAndGetAdminTwoAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response listUsersSuperAdmin(RequestBodyUsers requestBodyUsers) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/users")
                .body(requestBodyUsers)
                .token(Authorization.loginAndGetSuperAdminAccessToken())
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

    public static Response postRegistrationApplications(RequestBodyInstitution requestBodyInstitution) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/admin-registration-applications")
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
                .url("/api/v1/admin-registration-application")
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

    public static String generateApplicationIDForCompletedStatus() {
        RequestBodyInstitution requestBodyInstitution = RequestBodyInstitution.generateFilter(Pageable.generateFirstPage(), Filter.generate(ApplicationRegistrationSupportStatus.COMPLETED));
        Response response = postRegistrationApplications(requestBodyInstitution);
        if (response.getStatusCode() == 200) {
            return response.then().extract().jsonPath().getString("response.content[0].id");
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

    public static Response postRegistrationApplicationIDComplete(String applicationID, RegistrationApplicationCompletePayload requestBodyForRegistrationComplete) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/admin/registration-application/{id}/complete")
                .pathParameter(Map.of("id", applicationID))
                .body(requestBodyForRegistrationComplete)
                .token(Authorization.loginAndGetSuperAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response getAdminsPermissions() {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.GET)
                .url("/api/v1/permissions")
                .token(Authorization.loginAndGetAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response getSuperAdminsPermissions() {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.GET)
                .url("/api/v1/permissions")
                .token(Authorization.loginAndGetSuperAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response postEmergencyEvacuationApplications(ListEmergencyEvacuationApplications list) {
        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/emergency-evacuation-applications")
                .body(list)
                .token(Authorization.loginAndGetAdminAccessToken())
                .build();
        return AysRestAssured.perform(restAssuredRequest);

    }

    public static Response listRoles(RolesListPayload rolesListPayload, String token) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/roles")
                .body(rolesListPayload)
                .token(token)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response createRole(RoleCreatePayload roleCreatePayload) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/role")
                .body(roleCreatePayload)
                .token(Authorization.loginAndGetTestAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static String generateRoleId() {
        RoleCreatePayload roleCreatePayload = RoleCreatePayload.generate();
        Response response = createRole(roleCreatePayload);

        if (response.getStatusCode() == 200) {
            return DatabaseUtility.getLastCreatedRoleId();
        } else {
            throw new RuntimeException("Role creation failed with status code: " + response.getStatusCode());
        }
    }

    public static Response updateRole(String roleId, RoleUpdatePayload roleUpdatePayload) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.PUT)
                .url("/api/v1/role/{id}")
                .pathParameter(Map.of("id", roleId))
                .body(roleUpdatePayload)
                .token(Authorization.loginAndGetTestAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response deleteRole(String roleId) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.DELETE)
                .url("/api/v1/role/{id}")
                .pathParameter(Map.of("id", roleId))
                .token(Authorization.loginAndGetTestAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response patchActivateRole(String roleId, String token) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.PATCH)
                .url("/api/v1/role/{id}/activate")
                .pathParameter(Map.of("id", roleId))
                .token(token)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

    public static Response patchPassivateRole(String roleId, String token) {

        AysRestAssuredRequest restAssuredRequest = AysRestAssuredRequest.builder()
                .httpMethod(HttpMethod.PATCH)
                .url("/api/v1/role/{id}/passivate")
                .pathParameter(Map.of("id", roleId))
                .token(token)
                .build();

        return AysRestAssured.perform(restAssuredRequest);
    }

}