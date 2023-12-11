package tests.institution.assignmentmanagementservice;

import endpoints.InstitutionEndpoints;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import payload.FiltersForAssignments;
import payload.Helper;
import payload.PhoneNumber;
import payload.RequestBodyAssignments;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;

public class PostAssignmentsServiceTest {
    RequestBodyAssignments requestBodyAssignments;
    Logger logger = LogManager.getLogger(this.getClass());

    @Test(dataProvider = "filterScenarios")
    public void listAssignmentsWithFilters(String statuses, String countryCode, String lineNumber) {
        logger.info("Test case IAMS_01-06 is running.. ");
        requestBodyAssignments = Helper.createRequestBodyAssignments("1", "10");
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

    @Test(dataProvider = "paginationScenarios")
    public void postAssignmentsPagination(String page, String pageSize) {
        logger.info("Test case IAMS_08-12 is running..");
        requestBodyAssignments = Helper.createRequestBodyAssignments(page, pageSize);
        Response response = InstitutionEndpoints.listAssignments(requestBodyAssignments);
        response.then()
                .statusCode(400)
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("isSuccess", equalTo(false))
                .body("header", equalTo("VALIDATION ERROR"));


    }

    @Test()
    public void postAssignmentsMissingPagination() {
        logger.info("Test case IAMS_07 is running..");
        requestBodyAssignments = Helper.createRequestBodyAssignmentsMissingPagination();
        Response response = InstitutionEndpoints.listAssignments(requestBodyAssignments);
        response.then()
                .statusCode(400)
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("isSuccess", equalTo(false))
                .body("header", equalTo("VALIDATION ERROR"));


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

    @DataProvider(name = "filterScenarios")
    public Object[][] filterScenarios() {
        return new Object[][]{
                {"AVAILABLE", "90", "1234567890"}, //IAMS_06
                {null, "90", "1234567890"}, //IAMS_02
                {"AVAILABLE", null, "1234567890"}, //IAMS_04
                {"AVAILABLE", "90", null}, //IAMS_03
                {null, null, null}, // IAMS_01
        };
    }

    @DataProvider(name = "paginationScenarios")
    public Object[][] paginationScenarios() {
        return new Object[][]{
                {null, "10"}, // IAMS_08
                {"", "10"}, // IAMS_10
                {"1", null}, // IAMS_11
                {"1", ""}, // IAMS_12
                {"0", "10"}, // IAMS_09
                {"1", "20"} //
        };
    }
}
