package org.ays.utility;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import lombok.experimental.UtilityClass;
import org.ays.tests.database.aysInstitutionName.AfetYonetimSistemi;
import org.ays.tests.database.aysInstitutionName.DisasterFoundation;
import org.ays.tests.database.aysInstitutionName.VolunteerFoundation;
import org.hamcrest.Matchers;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@UtilityClass
public class AysResponseSpecs {

    public static ResponseSpecification expectSuccessResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType("application/json")
                .expectBody("time", notNullValue())
                .expectBody("isSuccess", equalTo(true))
                .build();
    }

    public static ResponseSpecification expectBadRequestResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(400)
                .expectContentType("application/json")
                .expectBody("time", notNullValue())
                .expectBody("header", equalTo("VALIDATION ERROR"))
                .expectBody("isSuccess", equalTo(false))
                .build();
    }

    public static ResponseSpecification expectNotFoundResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(404)
                .expectContentType("application/json")
                .expectBody("time", notNullValue())
                .expectBody("header", equalTo("NOT EXIST"))
                .expectBody("isSuccess", equalTo(false))
                .build();
    }

    public static ResponseSpecification expectUnauthorizedResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(401)
                .expectContentType("application/json")
                .expectBody("time", notNullValue())
                .expectBody("header", equalTo("AUTH ERROR"))
                .expectBody("isSuccess", equalTo(false))
                .build();
    }

    public static ResponseSpecification expectConflictResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(409)
                .expectContentType("application/json")
                .expectBody("time", notNullValue())
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

    public static ResponseSpecification expectUserDetailsInContent() {
        return new ResponseSpecBuilder()
                .expectBody("response.content[0].id", notNullValue())
                .expectBody("response.content[0].firstName", notNullValue())
                .expectBody("response.content[0].lastName", notNullValue())
                .expectBody("response.content[0].emailAddress", notNullValue())
                .expectBody("response.content[0].phoneNumber", notNullValue())
                .expectBody("response.content[0].city", notNullValue())
                .expectBody("response.content[0].status", notNullValue())
                .expectBody("response.content[0].createdAt", notNullValue())
                .expectBody("response.content[0].updatedAt", nullValue())
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

    public static ResponseSpecification expectInvalidPageableErrors() {
        return new ResponseSpecBuilder()
                .expectBody("subErrors*.message", everyItem(equalTo("must be between 1 and 99999999")))
                .expectBody("subErrors*.field", everyItem(Matchers.anyOf(equalTo("page"), equalTo("pageSize"))))
                .expectBody("subErrors*.type", everyItem(equalTo("int")))
                .build();
    }

    public static ResponseSpecification subErrorsSpec(ErrorMessage message, String field, String type) {
        return new ResponseSpecBuilder()
                .expectBody("subErrors[0].message", equalTo(message.getMessage()))
                .expectBody("subErrors[0].field", equalTo(field))
                .expectBody("subErrors[0].type", equalTo(type))
                .build();
    }

    public static ResponseSpecification expectTotalElementCountForVolunteer(int totalElementCount) {
        return new ResponseSpecBuilder()
                .expectBody("response.totalElementCount", equalTo(totalElementCount))
                .build();

    }

    public static ResponseSpecification expectTotalElementCountForDisaster(int totalElementCount) {
        return new ResponseSpecBuilder()
                .expectBody("response.totalElementCount", equalTo(totalElementCount))
                .build();

    }

    public static ResponseSpecification expectTotalElementCountForAYS(int totalElementCount) {
        return new ResponseSpecBuilder()
                .expectBody("response.totalElementCount", equalTo(totalElementCount))
                .build();

    }

}
