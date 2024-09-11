package org.ays.emergencyapplication.endpoints;

import io.restassured.response.Response;
import org.ays.common.model.payload.AysRestAssuredPayload;
import org.ays.common.util.AysRestAssured;
import org.ays.endpoints.Authorization;
import org.ays.payload.EmergencyEvacuationApplication;
import org.ays.payload.EmergencyEvacuationApplicationListPayload;
import org.openqa.selenium.remote.http.HttpMethod;

public class EmergencyEvacuationApplicationEndpoints {

    public static Response postEmergencyEvacuationApplications(EmergencyEvacuationApplicationListPayload list) {
        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/emergency-evacuation-applications")
                .body(list)
                .token(Authorization.loginAndGetAdminAccessToken())
                .build();
        return AysRestAssured.perform(restAssuredPayload);
    }

    public static Response postEmergencyEvacuationApplication(EmergencyEvacuationApplication emergencyEvacuationApplication) {
        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/emergency-evacuation-application")
                .body(emergencyEvacuationApplication)
                .build();
        return AysRestAssured.perform(restAssuredRequest);
    }

}
