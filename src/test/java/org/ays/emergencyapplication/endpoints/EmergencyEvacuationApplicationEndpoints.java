package org.ays.emergencyapplication.endpoints;

import io.restassured.response.Response;
import org.ays.common.model.request.AysRestAssuredPayload;
import org.ays.common.util.AysRestAssured;
import org.ays.payload.EmergencyEvacuationApplication;
import org.openqa.selenium.remote.http.HttpMethod;

public class EmergencyEvacuationApplicationEndpoints {

    public static Response postEmergencyEvacuationApplication(EmergencyEvacuationApplication emergencyEvacuationApplication) {
        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/emergency-evacuation-application")
                .body(emergencyEvacuationApplication)
                .build();
        return AysRestAssured.perform(restAssuredRequest);
    }

}
