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

    public static Response findAll(EmergencyEvacuationApplicationListPayload list) {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationAdmin();
        String accessToken = AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");

        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/emergency-evacuation-applications")
                .body(list)
                .token(accessToken)
                .build();
        return AysRestAssured.perform(restAssuredPayload);
    }

    public static Response create(EmergencyEvacuationApplicationPayload applicationPayload) {
        AysRestAssuredPayload restAssuredRequest = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/emergency-evacuation-application")
                .body(applicationPayload)
                .build();
        return AysRestAssured.perform(restAssuredRequest);
    }

}
