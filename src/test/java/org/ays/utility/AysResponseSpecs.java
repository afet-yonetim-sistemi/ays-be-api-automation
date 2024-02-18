package org.ays.utility;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import lombok.experimental.UtilityClass;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@UtilityClass
public class AysResponseSpecs {

    public static ResponseSpecification successResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType("application/json")
                .expectBody("time", notNullValue())
                .expectBody("httpStatus", equalTo("OK"))
                .expectBody("isSuccess", equalTo(true))
                .build();
    }

    public static ResponseSpecification badRequestResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(400)
                .expectContentType("application/json")
                .expectBody("time", notNullValue())
                .expectBody("httpStatus", equalTo("BAD_REQUEST"))
                .expectBody("header", equalTo("VALIDATION ERROR"))
                .expectBody("isSuccess", equalTo(false))
                .build();
    }

    public static ResponseSpecification unauthorizedResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(401)
                .expectContentType("application/json")
                .expectBody("time", notNullValue())
                .expectBody("httpStatus", equalTo("UNAUTHORIZED"))
                .expectBody("header", equalTo("AUTH ERROR"))
                .expectBody("isSuccess", equalTo(false))
                .build();
    }

    public static ResponseSpecification getTokenResponseSpec() {
        return new ResponseSpecBuilder()
                .expectBody("response.accessToken", notNullValue())
                .expectBody("response.accessTokenExpiresAt", notNullValue())
                .expectBody("response.refreshToken", notNullValue())
                .build();
    }

    public static ResponseSpecification nullCredentialFieldErrorSpec(String field) {
        return new ResponseSpecBuilder()
                .expectBody("subErrors[0].message", equalTo("must not be blank"))
                .expectBody("subErrors[0].field", equalTo(field))
                .expectBody("subErrors[0].type", equalTo("String"))
                .build();
    }

}
