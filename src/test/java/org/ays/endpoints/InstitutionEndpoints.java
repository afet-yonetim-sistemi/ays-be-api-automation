package org.ays.endpoints;

import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import org.ays.common.model.payload.AysRestAssuredPayload;
import org.ays.common.util.AysRestAssured;
import org.ays.payload.ListEmergencyEvacuationApplications;
import org.ays.payload.RoleCreatePayload;
import org.ays.payload.RoleUpdatePayload;
import org.ays.payload.RolesListPayload;
import org.ays.utility.DatabaseUtility;
import org.openqa.selenium.remote.http.HttpMethod;

import java.util.Map;

@UtilityClass
public class InstitutionEndpoints {

    public static Response getInstitutionsSummary() {
        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.GET)
                .url("/api/v1/institutions/summary")
                .token(Authorization.loginAndGetSuperAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredPayload);
    }

    public static Response getAdminsPermissions() {

        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.GET)
                .url("/api/v1/permissions")
                .token(Authorization.loginAndGetAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredPayload);
    }

    public static Response getSuperAdminsPermissions() {

        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.GET)
                .url("/api/v1/permissions")
                .token(Authorization.loginAndGetSuperAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredPayload);
    }

    public static Response postEmergencyEvacuationApplications(ListEmergencyEvacuationApplications list) {
        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/emergency-evacuation-applications")
                .body(list)
                .token(Authorization.loginAndGetAdminAccessToken())
                .build();
        return AysRestAssured.perform(restAssuredPayload);

    }

    public static Response listRoles(RolesListPayload rolesListPayload, String token) {

        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/roles")
                .body(rolesListPayload)
                .token(token)
                .build();

        return AysRestAssured.perform(restAssuredPayload);
    }

    public static Response createRole(RoleCreatePayload roleCreatePayload) {

        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/role")
                .body(roleCreatePayload)
                .token(Authorization.loginAndGetTestAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredPayload);
    }

    public static Response createRole(RoleCreatePayload roleCreatePayload, String token) {

        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/role")
                .body(roleCreatePayload)
                .token(token)
                .build();

        return AysRestAssured.perform(restAssuredPayload);
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

        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.PUT)
                .url("/api/v1/role/{id}")
                .pathParameter(Map.of("id", roleId))
                .body(roleUpdatePayload)
                .token(Authorization.loginAndGetTestAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredPayload);
    }

    public static Response deleteRole(String roleId) {

        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.DELETE)
                .url("/api/v1/role/{id}")
                .pathParameter(Map.of("id", roleId))
                .token(Authorization.loginAndGetTestAdminAccessToken())
                .build();

        return AysRestAssured.perform(restAssuredPayload);
    }

    public static Response patchActivateRole(String roleId, String token) {

        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.PATCH)
                .url("/api/v1/role/{id}/activate")
                .pathParameter(Map.of("id", roleId))
                .token(token)
                .build();

        return AysRestAssured.perform(restAssuredPayload);
    }

    public static Response patchPassivateRole(String roleId, String token) {

        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.PATCH)
                .url("/api/v1/role/{id}/passivate")
                .pathParameter(Map.of("id", roleId))
                .token(token)
                .build();

        return AysRestAssured.perform(restAssuredPayload);
    }

}