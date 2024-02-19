package org.ays.endpoints;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.experimental.UtilityClass;
import org.ays.utility.AysConfigurationProperty;
import org.testng.Reporter;

@UtilityClass
public class AysRestAssured {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static Response perform(AysRestAssuredRequest restAssuredRequest) {

        Reporter.log("---- Rest Assured Request ----");
        Reporter.log(GSON.toJson(restAssuredRequest));

        RequestSpecification requestSpecification = RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .when();

        if (restAssuredRequest.isTokenExist()) {
            requestSpecification
                    .header("Authorization", "Bearer ".concat(restAssuredRequest.getToken()));
        }

        restAssuredRequest.getPathParameter().forEach(requestSpecification::pathParam);

        if (restAssuredRequest.isRequestBodyExist()) {
            requestSpecification.body(restAssuredRequest.getBody());
        }

        String url = AysConfigurationProperty.Api.URL.concat(restAssuredRequest.getUrl());
        Response response = (switch (restAssuredRequest.getHttpMethod()) {
            case GET -> requestSpecification.get(url);
            case POST -> requestSpecification.post(url);
            case PUT -> requestSpecification.put(url);
            case DELETE -> requestSpecification.delete(url);
            default -> throw new UnsupportedOperationException();
        }).then()
                .log().all()
                .extract().response();

        Reporter.log("---- API Response ----");
        Reporter.log(response.asPrettyString());

        return response;
    }

}
