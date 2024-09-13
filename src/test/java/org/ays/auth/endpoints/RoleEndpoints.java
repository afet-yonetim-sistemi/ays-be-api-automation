package org.ays.auth.endpoints;

import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import org.ays.auth.datasource.RoleDataSource;
import org.ays.auth.payload.LoginPayload;
import org.ays.auth.payload.RoleCreatePayload;
import org.ays.auth.payload.RoleListPayload;
import org.ays.auth.payload.RoleUpdatePayload;
import org.ays.common.model.payload.AysRestAssuredPayload;
import org.ays.common.util.AysRestAssured;
import org.openqa.selenium.remote.http.HttpMethod;

import java.util.Map;

@UtilityClass
public class RoleEndpoints {

    public static Response listRoles(RoleListPayload roleListPayload, String token) {

        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/roles")
                .body(roleListPayload)
                .token(token)
                .build();

        return AysRestAssured.perform(restAssuredPayload);
    }

    public static Response createRole(RoleCreatePayload roleCreatePayload, String accessToken) {

        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/role")
                .body(roleCreatePayload)
                .token(accessToken)
                .build();

        return AysRestAssured.perform(restAssuredPayload);
    }

    public static String generateRoleId() {

        LoginPayload loginPayload = LoginPayload.generateAsTestAdmin();
        String accessToken = AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");

        RoleCreatePayload roleCreatePayload = RoleCreatePayload.generate();
        Response response = createRole(roleCreatePayload, accessToken);

        if (response.getStatusCode() == 200) {
            return RoleDataSource.findLastRoleId();
        } else {
            throw new RuntimeException("Role creation failed with status code: " + response.getStatusCode());
        }
    }

    public static Response updateRole(String roleId, RoleUpdatePayload roleUpdatePayload, String accessToken) {

        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.PUT)
                .url("/api/v1/role/{id}")
                .pathParameter(Map.of("id", roleId))
                .body(roleUpdatePayload)
                .token(accessToken)
                .build();

        return AysRestAssured.perform(restAssuredPayload);
    }

    public static Response updateActivateRole(String roleId, String token) {

        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.PATCH)
                .url("/api/v1/role/{id}/activate")
                .pathParameter(Map.of("id", roleId))
                .token(token)
                .build();

        return AysRestAssured.perform(restAssuredPayload);
    }

    public static Response updatePassivateRole(String roleId, String token) {

        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.PATCH)
                .url("/api/v1/role/{id}/passivate")
                .pathParameter(Map.of("id", roleId))
                .token(token)
                .build();

        return AysRestAssured.perform(restAssuredPayload);
    }

    public static Response deleteRole(String roleId) {

        LoginPayload loginPayload = LoginPayload.generateAsTestAdmin();
        String accessToken = AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");

        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.DELETE)
                .url("/api/v1/role/{id}")
                .pathParameter(Map.of("id", roleId))
                .token(accessToken)
                .build();

        return AysRestAssured.perform(restAssuredPayload);
    }



}