package org.ays.tests.institution.assignmentmanagementservice;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.payload.Assignment;
import org.ays.payload.AssignmentsFilter;
import org.ays.payload.Pagination;
import org.ays.payload.PhoneNumber;
import org.ays.payload.RequestBodyAssignments;
import org.ays.utility.AysResponseSpecs;
import org.ays.utility.DataProvider;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

public class PostAssignmentsTest {
    RequestBodyAssignments requestBodyAssignments;

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        requestBodyAssignments = new RequestBodyAssignments();
    }

    @Test(groups = {"Smoke", "Regression", "Institution"}, dataProvider = "positivePaginationData", dataProviderClass = DataProvider.class)
    public void listAssignmentsWithPositivePaginationScenarios(int page, int pageSize) {
        requestBodyAssignments.setPagination(Pagination.generate(page, pageSize));
        Response response = InstitutionEndpoints.listAssignments(requestBodyAssignments);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectUnfilteredListResponseSpec());
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "negativePaginationData", dataProviderClass = DataProvider.class)
    public void listAssignmentsWithNegativePaginationScenarios(int page, int pageSize) {
        requestBodyAssignments = RequestBodyAssignments.generateRequestBodyAssignments(page, pageSize);
        Response response = InstitutionEndpoints.listAssignments(requestBodyAssignments);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must be between 1 and 99999999"))
                .body("subErrors[0].field", anyOf(equalTo("page"), equalTo("pageSize")))
                .body("subErrors[0].type", equalTo("int"));

    }

    @Test(groups = {"Regression", "Institution"})
    public void listAssignmentsWithNullPagination() {
        Pagination pagination = new Pagination();
        requestBodyAssignments.setPagination(pagination);
        Response response = InstitutionEndpoints.listAssignments(requestBodyAssignments);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.expectInvalidPaginationErrors());

    }

    @Test(groups = {"Regression", "Smoke", "Institution"}, description = "Prior to executing this method, an assignment is created to prevent failures in case no user is associated with the institution.")
    public void listAssignmentsWithValidPhoneNumberFilter() {
        Assignment assignment = InstitutionEndpoints.generateANewAssignment();
        requestBodyAssignments.setFilter(AssignmentsFilter.generate(assignment.getPhoneNumber(), null));
        requestBodyAssignments.setPagination(Pagination.generateFirstPage());
        Response response = InstitutionEndpoints.listAssignments(requestBodyAssignments);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectAssignmentDetailsInContent())
                .spec(AysResponseSpecs.expectDefaultListingDetails())
                .body("response.filteredBy.statuses", equalTo(null))
                .body("response.filteredBy.phoneNumber.countryCode", equalTo(assignment.getPhoneNumber().getCountryCode()))
                .body("response.filteredBy.phoneNumber.lineNumber", equalTo(assignment.getPhoneNumber().getLineNumber()));

    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidPhoneNumberDataForFilter", dataProviderClass = DataProvider.class)
    public void listAssignmentsWithInvalidPhoneNumberFilter(String countryCode, String lineNumber, String errorMessage) {
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setCountryCode(countryCode);
        phoneNumber.setLineNumber(lineNumber);
        requestBodyAssignments.setPagination(Pagination.generateFirstPage());
        requestBodyAssignments.setFilter(AssignmentsFilter.generate(phoneNumber, null));
        Response response = InstitutionEndpoints.listAssignments(requestBodyAssignments);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", either(equalTo("lineNumber")).or(equalTo("countryCode")))
                .body("subErrors[0].type", equalTo("String"));

    }

    @Test(groups = {"Regression", "Smoke", "Institution"}, description = "Prior to executing this method, an assignment is created to prevent failures in case no user is associated with the institution.")
    public void listAssignmentsWithValidStatusFilter() {
        InstitutionEndpoints.generateANewAssignment();
        List<String> statuses = new ArrayList<>();
        statuses.add("AVAILABLE");
        requestBodyAssignments.setPagination(Pagination.generateFirstPage());
        requestBodyAssignments.setFilter(AssignmentsFilter.generate(null, statuses));
        Response response = InstitutionEndpoints.listAssignments(requestBodyAssignments);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectAssignmentDetailsInContent())
                .spec(AysResponseSpecs.expectDefaultListingDetails())
                .body("response.sortedBy", equalTo(null))
                .body("response.filteredBy.statuses", hasItem("AVAILABLE"))
                .body("response.filteredBy.phoneNumber.countryCode", equalTo(null))
                .body("response.filteredBy.phoneNumber.lineNumber", equalTo(null));
    }

    @Test(groups = {"Regression", "Institution"}, description = "Prior to executing this method, an assignment is created to prevent failures in case no user is associated with the institution.")
    public void listAssignmentsWithInvalidStatus() {
        InstitutionEndpoints.generateANewAssignment();
        List<String> statuses = new ArrayList<>();
        statuses.add("available");
        statuses.add("ASSIGNED");
        requestBodyAssignments.setPagination(Pagination.generateFirstPage());
        requestBodyAssignments.setFilter(AssignmentsFilter.generate(null, statuses));
        Response response = InstitutionEndpoints.listAssignments(requestBodyAssignments);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec());
    }

    @Test(groups = {"Regression", "Smoke", "Institution"}, description = "Prior to executing this method, an assignment is created to prevent failures in case no user is associated with the institution.")
    public void listAssignmentsWithValidStatusAndPhoneNumber() {
        Assignment assignment = InstitutionEndpoints.generateANewAssignment();
        List<String> statuses = new ArrayList<>();
        statuses.add("AVAILABLE");
        requestBodyAssignments.setPagination(Pagination.generateFirstPage());
        requestBodyAssignments.setFilter(AssignmentsFilter.generate(assignment.getPhoneNumber(), statuses));
        Response response = InstitutionEndpoints.listAssignments(requestBodyAssignments);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectAssignmentDetailsInContent())
                .spec(AysResponseSpecs.expectDefaultListingDetails())
                .body("response.filteredBy.statuses", hasItem("AVAILABLE"))
                .body("response.filteredBy.phoneNumber.lineNumber", equalTo(assignment.getPhoneNumber().getLineNumber()))
                .body("response.filteredBy.phoneNumber.countryCode", equalTo(assignment.getPhoneNumber().getCountryCode()));

    }

}
