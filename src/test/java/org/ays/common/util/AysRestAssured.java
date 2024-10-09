package org.ays.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.experimental.UtilityClass;
import org.ays.common.model.payload.AysRestAssuredPayload;
import org.testng.Reporter;

@UtilityClass
public class AysRestAssured {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static Response perform(AysRestAssuredPayload restAssuredPayload) {

        Reporter.log("---- Rest Assured Request ----");
        Reporter.log(GSON.toJson(restAssuredPayload));

        RequestSpecification requestSpecification = RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .when();

        if (restAssuredPayload.isTokenExist()) {
            requestSpecification
                    .header("Authorization", "Bearer ".concat(restAssuredPayload.getToken()));
        }

        restAssuredPayload.getPathParameter().forEach(requestSpecification::pathParam);

        if (restAssuredPayload.isRequestBodyExist()) {
            requestSpecification.body(restAssuredPayload.getBody());
        }

        String url = AysConfigurationProperty.Api.URL.concat(restAssuredPayload.getUrl());
        Response response = (switch (restAssuredPayload.getHttpMethod()) {
            case GET -> requestSpecification.get(url);
            case POST -> requestSpecification.post(url);
            case PUT -> requestSpecification.put(url);
            case DELETE -> requestSpecification.delete(url);
            case PATCH -> requestSpecification.patch(url);
            default -> throw new UnsupportedOperationException();
        }).then()
                .log().all()
                .extract().response();

        Reporter.log("---- API Response ----");
        Reporter.log(response.asPrettyString());

        return response;
    }

}
