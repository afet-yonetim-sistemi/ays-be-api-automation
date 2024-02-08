package endpoints;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

        Response response = (switch (restAssuredRequest.getHttpMethod()) {
            case GET -> requestSpecification.get(restAssuredRequest.getUrl());
            case POST -> requestSpecification.post(restAssuredRequest.getUrl());
            case PUT -> requestSpecification.put(restAssuredRequest.getUrl());
            case DELETE -> requestSpecification.delete(restAssuredRequest.getUrl());
            default -> throw new UnsupportedOperationException();
        }).then()
                .log().all()
                .extract().response();

        Reporter.log("---- API Response ----");
        Reporter.log(response.asPrettyString());

        return response;
    }

}
