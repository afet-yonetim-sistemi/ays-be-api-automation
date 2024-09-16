package org.ays.auth.endpoints;

import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
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

    public static Response deleteRole(String roleId, String token) {

        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.DELETE)
                .url("/api/v1/role/{id}")
                .pathParameter(Map.of("id", roleId))
                .token(token)
                .build();

        return AysRestAssured.perform(restAssuredPayload);
    }

    public static String createAndReturnRoleId(RoleCreatePayload role, String token) {
        RoleEndpoints.createRole(role, token);
        RoleListPayload list = RoleListPayload.generateWithFilter(role);
        return RoleEndpoints.listRoles(list, token).jsonPath().getString("response.content[0].id");
    }


}