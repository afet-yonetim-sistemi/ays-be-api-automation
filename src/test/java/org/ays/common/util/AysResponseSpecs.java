package org.ays.common.util;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import lombok.experimental.UtilityClass;
import org.ays.common.model.enums.AysErrorMessage;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.AllOf.allOf;

@UtilityClass
public class AysResponseSpecs {

    private static final String UUID_REGEX = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    private static final String TIME_FORMAT_REGEX = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{1,9}";

    public static ResponseSpecification expectSuccessResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType("application/json")
                .expectBody("time", notNullValue())
                .expectBody("time", matchesPattern(TIME_FORMAT_REGEX))
                .expectBody("code", matchesPattern(UUID_REGEX))
                .expectBody("isSuccess", equalTo(true))
                .build();
    }

    public static ResponseSpecification expectBadRequestResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(400)
                .expectContentType("application/json")
                .expectBody("time", notNullValue())
                .expectBody("time", matchesPattern(TIME_FORMAT_REGEX))
                .expectBody("code", matchesPattern(UUID_REGEX))
                .expectBody("header", equalTo("VALIDATION ERROR"))
                .expectBody("isSuccess", equalTo(false))
                .build();
    }

    public static ResponseSpecification expectNotFoundResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(404)
                .expectContentType("application/json")
                .expectBody("time", notNullValue())
                .expectBody("time", matchesPattern(TIME_FORMAT_REGEX))
                .expectBody("code", matchesPattern(UUID_REGEX))
                .expectBody("header", equalTo("NOT EXIST ERROR"))
                .expectBody("isSuccess", equalTo(false))
                .build();
    }

    public static ResponseSpecification expectUnauthorizedResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(401)
                .expectContentType("application/json")
                .expectBody("time", notNullValue())
                .expectBody("time", matchesPattern(TIME_FORMAT_REGEX))
                .expectBody("code", matchesPattern(UUID_REGEX))
                .expectBody("header", equalTo("AUTH ERROR"))
                .expectBody("isSuccess", equalTo(false))
                .build();
    }

    public static ResponseSpecification expectForbiddenResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(403)
                .expectContentType("application/json")
                .expectBody("time", notNullValue())
                .expectBody("time", matchesPattern(TIME_FORMAT_REGEX))
                .expectBody("code", matchesPattern(UUID_REGEX))
                .expectBody("header", equalTo("AUTH ERROR"))
                .expectBody("isSuccess", equalTo(false))
                .build();
    }

    public static ResponseSpecification expectConflictResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(409)
                .expectContentType("application/json")
                .expectBody("time", notNullValue())
                .expectBody("time", matchesPattern(TIME_FORMAT_REGEX))
                .expectBody("code", matchesPattern(UUID_REGEX))
                .expectBody("header", equalTo("CONFLICT ERROR"))
                .expectBody("isSuccess", equalTo(false))
                .build();
    }

    public static ResponseSpecification expectGetTokenResponseSpec() {
        return new ResponseSpecBuilder()
                .expectBody("response.accessToken", notNullValue())
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

    public static ResponseSpecification expectUnfilteredListResponseSpecForEmergencyEvacuationApplications() {
        return new ResponseSpecBuilder()
                .expectBody("response.content", instanceOf(List.class))
                .expectBody("response.pageNumber", notNullValue())
                .expectBody("response.totalPageCount", notNullValue())
                .expectBody("response.totalElementCount", notNullValue())
                .expectBody("response.sortedBy", equalTo(null))
                .expectBody("response.filteredBy.referenceNumber", equalTo(null))
                .expectBody("response.filteredBy.sourceCity", equalTo(null))
                .expectBody("response.filteredBy.sourceDistrict", equalTo(null))
                .expectBody("response.filteredBy.seatingCount", equalTo(null))
                .expectBody("response.filteredBy.targetCity", equalTo(null))
                .expectBody("response.filteredBy.targetDistrict", equalTo(null))
                .expectBody("response.filteredBy.statuses", equalTo(null))
                .expectBody("response.filteredBy.isInPerson", equalTo(null))
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

    public static ResponseSpecification subErrorsSpec(AysErrorMessage errorMessage, String field, String type) {
        return new ResponseSpecBuilder()
                .expectBody("subErrors", hasItems(
                        allOf(
                                hasEntry("message", errorMessage.getMessage()),
                                hasEntry("field", field),
                                hasEntry("type", type)
                        ))).build();
    }

    public static ResponseSpecification expectTotalElementCount(int totalElementCount) {
        return new ResponseSpecBuilder()
                .expectBody("response.totalElementCount", equalTo(totalElementCount))
                .build();

    }

    public static ResponseSpecification expectRolesListInContent() {
        return new ResponseSpecBuilder()
                .expectBody("response.content[0].id", notNullValue())
                .expectBody("response.content[0].name", notNullValue())
                .expectBody("response.content[0].status", notNullValue())
                .expectBody("response.content[0].createdAt", notNullValue())
                .build();
    }

}
