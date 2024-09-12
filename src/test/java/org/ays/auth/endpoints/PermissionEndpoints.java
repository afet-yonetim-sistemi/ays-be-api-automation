package org.ays.auth.endpoints;

import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import org.ays.auth.payload.LoginPayload;
import org.ays.common.model.payload.AysRestAssuredPayload;
import org.ays.common.util.AysRestAssured;
import org.openqa.selenium.remote.http.HttpMethod;

@UtilityClass
public class PermissionEndpoints {

    public static Response getAdminsPermissions() {

        LoginPayload loginPayload = LoginPayload.generateAsAdminUserOne();
        String accessToken = AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");

        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.GET)
                .url("/api/v1/permissions")
                .token(accessToken)
                .build();

        return AysRestAssured.perform(restAssuredPayload);
    }

    public static Response getSuperAdminsPermissions() {

        LoginPayload loginPayload = LoginPayload.generateAsSuperAdminUserOne();
        String accessToken = AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");

        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.GET)
                .url("/api/v1/permissions")
                .token(accessToken)
                .build();

        return AysRestAssured.perform(restAssuredPayload);
    }

}