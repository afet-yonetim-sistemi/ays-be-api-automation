package org.ays.utility;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import lombok.experimental.UtilityClass;
import org.hamcrest.Matchers;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;

@UtilityClass
public class AysResponseSpecs {

    public static ResponseSpecification expectSuccessResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType("application/json")
                .expectBody("time", notNullValue())
                .expectBody("httpStatus", equalTo("OK"))
                .expectBody("isSuccess", equalTo(true))
                .build();
    }

    public static ResponseSpecification expectBadRequestResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(400)
                .expectContentType("application/json")
                .expectBody("time", notNullValue())
                .expectBody("httpStatus", equalTo("BAD_REQUEST"))
                .expectBody("header", equalTo("VALIDATION ERROR"))
                .expectBody("isSuccess", equalTo(false))
                .build();
    }

    public static ResponseSpecification expectNotFoundResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(404)
                .expectContentType("application/json")
                .expectBody("time", notNullValue())
                .expectBody("httpStatus", equalTo("NOT_FOUND"))
                .expectBody("header", equalTo("NOT EXIST"))
                .expectBody("isSuccess", equalTo(false))
                .build();
    }

    public static ResponseSpecification expectUnauthorizedResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(401)
                .expectContentType("application/json")
                .expectBody("time", notNullValue())
                .expectBody("httpStatus", equalTo("UNAUTHORIZED"))
                .expectBody("header", equalTo("AUTH ERROR"))
                .expectBody("isSuccess", equalTo(false))
                .build();
    }

    public static ResponseSpecification expectConflictResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(409)
                .expectContentType("application/json")
                .expectBody("time", notNullValue())
                .expectBody("httpStatus", equalTo("CONFLICT"))
                .expectBody("header", equalTo("ALREADY EXIST"))
                .expectBody("isSuccess", equalTo(false))
                .build();
    }

    public static ResponseSpecification expectGetTokenResponseSpec() {
        return new ResponseSpecBuilder()
                .expectBody("response.accessToken", notNullValue())
                .expectBody("response.accessTokenExpiresAt", notNullValue())
                .expectBody("response.refreshToken", notNullValue())
                .build();
    }

    public static ResponseSpecification expectNullCredentialFieldErrorSpec(String field) {
        return new ResponseSpecBuilder()
                .expectBody("subErrors[0].message", equalTo("must not be blank"))
                .expectBody("subErrors[0].field", equalTo(field))
                .expectBody("subErrors[0].type", equalTo("String"))
                .build();
    }

    public static ResponseSpecification expectUnfilteredListResponseSpec() {
        return new ResponseSpecBuilder()
                .expectBody("response.content", instanceOf(List.class))
                .expectBody("response.pageNumber", notNullValue())
                .expectBody("response.totalPageCount", notNullValue())
                .expectBody("response.totalElementCount", notNullValue())
                .expectBody("response.sortedBy", equalTo(null))
                .expectBody("response.filteredBy", equalTo(null))
                .build();
    }

    public static ResponseSpecification expectAssignmentDetailsInContent() {
        return new ResponseSpecBuilder()
                .expectBody("response.content[0].createdAt", notNullValue())
                .expectBody("response.content[0].createdUser", notNullValue())
                .expectBody("response.content[0].id", notNullValue())
                .expectBody("response.content[0].status", notNullValue())
                .expectBody("response.content[0].description", notNullValue())
                .expectBody("response.content[0].firstName", notNullValue())
                .expectBody("response.content[0].lastName", notNullValue())
                .expectBody("response.content[0].location", notNullValue())
                .build();
    }

    public static ResponseSpecification expectUserDetailsInContent() {
        return new ResponseSpecBuilder()
                .expectBody("response.content[0].id", notNullValue())
                .expectBody("response.content[0].firstName", notNullValue())
                .expectBody("response.content[0].lastName", notNullValue())
                .expectBody("response.content[0].status", notNullValue())
                .expectBody("response.content[0].role", notNullValue())
                .expectBody("response.content[0].supportStatus", notNullValue())
                .expectBody("response.content[0].createdAt", notNullValue())
                .build();
    }

    public static ResponseSpecification expectDefaultListingDetails() {
        return new ResponseSpecBuilder()
                .expectBody("response.content", instanceOf(List.class))
                .expectBody("response.pageNumber", notNullValue())
                .expectBody("response.totalPageCount", notNullValue())
                .expectBody("response.totalElementCount", notNullValue())
                .build();
    }

    public static ResponseSpecification expectInvalidPaginationErrors() {
        return new ResponseSpecBuilder()
                .expectBody("subErrors*.message", everyItem(equalTo("must be between 1 and 99999999")))
                .expectBody("subErrors*.field", everyItem(Matchers.anyOf(equalTo("page"), equalTo("pageSize"))))
                .expectBody("subErrors*.type", everyItem(equalTo("int")))
                .build();
    }

}
