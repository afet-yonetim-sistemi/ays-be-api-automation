package org.ays.endpoints;

import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import org.ays.common.model.payload.AysRestAssuredPayload;
import org.ays.common.util.AysRestAssured;
import org.ays.payload.ListEmergencyEvacuationApplications;
import org.openqa.selenium.remote.http.HttpMethod;

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

    public static Response postEmergencyEvacuationApplications(ListEmergencyEvacuationApplications list) {
        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.POST)
                .url("/api/v1/emergency-evacuation-applications")
                .body(list)
                .token(Authorization.loginAndGetAdminAccessToken())
                .build();
        return AysRestAssured.perform(restAssuredPayload);

    }

}