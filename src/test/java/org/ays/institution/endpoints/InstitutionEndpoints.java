package org.ays.institution.endpoints;

import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import org.ays.common.model.payload.AysRestAssuredPayload;
import org.ays.common.util.AysRestAssured;
import org.openqa.selenium.remote.http.HttpMethod;

@UtilityClass
public class InstitutionEndpoints {

    public static Response getInstitutionsSummary(String accessToken) {
        AysRestAssuredPayload restAssuredPayload = AysRestAssuredPayload.builder()
                .httpMethod(HttpMethod.GET)
                .url("/api/v1/institutions/summary")
                .token(accessToken)
                .build();

        return AysRestAssured.perform(restAssuredPayload);
    }

}
