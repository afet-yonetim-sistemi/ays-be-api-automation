package org.ays.emergencyapplication.endpoints;

import io.restassured.response.Response;
import org.ays.auth.endpoints.AuthEndpoints;
import org.ays.auth.payload.LoginPayload;
import org.ays.common.model.payload.AysRestAssuredPayload;
import org.ays.common.util.AysRestAssured;
import org.ays.emergencyapplication.model.payload.EmergencyEvacuationApplicationListPayload;
import org.ays.emergencyapplication.model.payload.EmergencyEvacuationApplicationPayload;
import org.openqa.selenium.remote.http.HttpMethod;

public class EmergencyEvacuationApplicationEndpoints {

    public static Response postEmergencyEvacuationApplications(EmergencyEvacuationApplicationListPayload list) {

        LoginPayload loginPayload = LoginPayload.generateAsAdminUserOne();
        String accessToken = AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");

        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/emergency-evacuation-applications")
                .body(list)
                .token(accessToken)
                .build();
        return AysRestAssured.perform(restAssuredPayload);
    }

    public static Response postEmergencyEvacuationApplication(EmergencyEvacuationApplicationPayload emergencyEvacuationApplicationPayload) {
        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/emergency-evacuation-application")
                .body(emergencyEvacuationApplicationPayload)
                .build();
        return AysRestAssured.perform(restAssuredRequest);
    }

}
