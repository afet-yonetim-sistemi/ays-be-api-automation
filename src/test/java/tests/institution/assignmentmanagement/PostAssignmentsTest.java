package tests.institution.assignmentmanagement;

import endpoints.InstitutionEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import payload.FiltersForAssignments;
import payload.Helper;
import payload.PhoneNumber;
import payload.RequestBodyAssignments;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;

public class PostAssignmentsTest extends InstitutionEndpoints {
    RequestBodyAssignments requestBodyAssignments;

    @BeforeMethod
    public void setUp() {
        requestBodyAssignments = Helper.createRequestBodyAssignments();
    }

    @DataProvider(name = "filterScenarios")
    public Object[][] filterScenarios() {
        return new Object[][]{
                {"AVAILABLE", "90", "1234567890"}, //all filters
                {null, "90", "1234567890"}, //without status
                {"AVAILABLE", null, "1234567890"}, //without country code
                {"AVAILABLE", "90", null}, //without lineNumber
                {null, null, "1234567890"}, //without status and country code
                {"AVAILABLE,IN_PROGRESS", "90", "1234567890"}, //with multiple status
                {"DONE", "90", "1234567890"}, //with done status
                {null, null, null}, //without filters
        };
    }
    @Test(dataProvider = "filterScenarios")
    public void listAssignmentsWithFilters(String statuses, String countryCode, String lineNumber) {
        requestBodyAssignments.setFilter(createFilter(statuses, countryCode, lineNumber));

        Response response = InstitutionEndpoints.listAssignments(requestBodyAssignments);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("response.content", notNullValue())
                .body("response.sortedBy", nullValue());

        if (statuses != null) {
            response.then()
                    .body("response.filteredBy.statuses", equalTo(Arrays.asList(statuses.split(","))));
        } else {
            response.then()
                    .body("response.filteredBy.statuses", nullValue());
        }

        if (countryCode != null) {
            response.then()
                    .body("response.filteredBy.phoneNumber.countryCode", equalTo(countryCode));
        } else {
            response.then()
                    .body("response.filteredBy.phoneNumber.countryCode", nullValue());
        }

        if (lineNumber != null) {
            response.then()
                    .body("response.filteredBy.phoneNumber.lineNumber", equalTo(lineNumber));
        } else {
            response.then()
                    .body("response.filteredBy.phoneNumber.lineNumber", nullValue());
        }
    }
    private FiltersForAssignments createFilter(String statuses, String countryCode, String lineNumber) {
        FiltersForAssignments filter = new FiltersForAssignments();
        if (statuses != null) {
            filter.setStatuses(Arrays.asList(statuses.split(",")));
        }
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setCountryCode(countryCode);
        phoneNumber.setLineNumber(lineNumber);
        filter.setPhoneNumber(phoneNumber);

        return filter;
    }
}
