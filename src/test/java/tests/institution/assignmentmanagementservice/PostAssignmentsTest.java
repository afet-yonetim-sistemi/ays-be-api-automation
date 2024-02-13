package tests.institution.assignmentmanagementservice;

import endpoints.InstitutionEndpoints;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import payload.*;

import java.util.List;

import static org.hamcrest.Matchers.*;

public class PostAssignmentsTest extends utility.DataProvider {
    RequestBodyAssignments requestBodyAssignments;

    @BeforeMethod
    public void setup() {
        requestBodyAssignments = new RequestBodyAssignments();
    }


    @Test(dataProvider = "positivePaginationData")
    @Story("As an admin user when I request data without providing any filter criteria, the system should return all available data without applying any filtering.")
    @Severity(SeverityLevel.NORMAL)
    public void listAssignmentsWithPositivePaginationScenarios(int page, int pageSize) {
        requestBodyAssignments.setPagination(Helper.setPagination(page, pageSize));
        Response response = InstitutionEndpoints.listAssignments(requestBodyAssignments);
        response.then()
                .spec(successResponseSpec())
                .body("response.content", instanceOf(List.class))
                .body("response.pageNumber", equalTo(page))
                .body("response.totalPageCount", notNullValue())
                .body("response.totalElementCount", notNullValue())
                .body("response.sortedBy", equalTo(null))
                .body("response.filteredB", equalTo(null));

    }

    @Test(dataProvider = "negativePaginationData")
    @Story("As an admin when I request list all assignments I want to get a proper error message when pagination values are invalid")
    @Severity(SeverityLevel.NORMAL)
    public void listAssignmentsWithNegativePaginationScenarios(int page, int pageSize) {
        requestBodyAssignments = Helper.createRequestBodyAssignments(page, pageSize);
        Response response = InstitutionEndpoints.listAssignments(requestBodyAssignments);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must be between 1 and 99999999"))
                .body("subErrors[0].field", anyOf(equalTo("page"), equalTo("pageSize")))
                .body("subErrors[0].type", equalTo("int"));

    }

    @Test()
    @Story("As an admin when I request list all assignments as pagination values are null I want to get a proper error message")
    @Severity(SeverityLevel.NORMAL)
    public void listAssignmentsWithNullPagination() {
        Pagination pagination = new Pagination();
        requestBodyAssignments.setPagination(pagination);
        Response response = InstitutionEndpoints.listAssignments(requestBodyAssignments);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must be between 1 and 99999999"))
                .body("subErrors[0].field", anyOf(equalTo("page"), equalTo("pageSize")))
                .body("subErrors[0].type", equalTo("int"))
                .body("subErrors[1].message", equalTo("must be between 1 and 99999999"))
                .body("subErrors[1].field", anyOf(equalTo("page"), equalTo("pageSize")))
                .body("subErrors[1].type", equalTo("int"));

    }

    @Test()
    @Story("As an admin I want to filter assignments by phone number")
    @Severity(SeverityLevel.NORMAL)
    @Description("Prior to executing this method, an assignment is created to prevent failures in case no user is associated with the institution.")
    public void listAssignmentsWithValidPhoneNumberFilter() {
        Assignment assignment = Helper.createANewAssignment();
        requestBodyAssignments.setFilter(Helper.createFilterWithAssignmentPhoneNumber(assignment.getPhoneNumber()));
        requestBodyAssignments.setPagination(Helper.createPagination());
        Response response = InstitutionEndpoints.listAssignments(requestBodyAssignments);
        response.then()
                .spec(successResponseSpec())
                .spec(listAssignmentsResponseSpec(assignment))
                .body("response.filteredBy.statuses", equalTo(null))
                .body("response.filteredBy.phoneNumber.countryCode", equalTo(assignment.getPhoneNumber().getCountryCode()))
                .body("response.filteredBy.phoneNumber.lineNumber", equalTo(assignment.getPhoneNumber().getLineNumber()));

    }

    @Test(dataProvider = "invalidPhoneNumberData")
    @Story("As an admin I want to get a proper error message when I filter assignments with invalid phone number")
    @Severity(SeverityLevel.NORMAL)
    public void listAssignmentsWithInvalidPhoneNumberFilter(String countryCode, String lineNumber) {
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setCountryCode(countryCode);
        phoneNumber.setLineNumber(lineNumber);
        requestBodyAssignments.setPagination(Helper.createPagination());
        requestBodyAssignments.setFilter(Helper.createFilterWithAssignmentPhoneNumber(phoneNumber));
        Response response = InstitutionEndpoints.listAssignments(requestBodyAssignments);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors[0].message", equalTo("MUST BE VALID"))
                .body("subErrors[0].field", equalTo("phoneNumber"))
                .body("subErrors[0].type", equalTo("AysPhoneNumberFilterRequest"));

    }

    @Test()
    @Story("As an admin I want to filter assignments by assignment status")
    @Severity(SeverityLevel.NORMAL)
    @Description("Prior to executing this method, an assignment is created to prevent failures in case no assignment is associated with the institution.")
    public void listAssignmentsWithValidStatus() {
        Helper.createANewAssignment();
        requestBodyAssignments.setPagination(Helper.createPagination());
        requestBodyAssignments.setFilter(Helper.createFilterWithAssignmentStatus("AVAILABLE"));
        Response response = InstitutionEndpoints.listAssignments(requestBodyAssignments);
        response.then()
                .spec(successResponseSpec())
                .body("response.content[0].createdAt", notNullValue())
                .body("response.content[0].createdUser", notNullValue())
                .body("response.content[0].id", notNullValue())
                .body("response.content[0].status", notNullValue())
                .body("response.content[0].description", notNullValue())
                .body("response.content[0].firstName", notNullValue())
                .body("response.content[0].lastName", notNullValue())
                .body("response.content[0].location", notNullValue())
                .body("response.pageNumber", equalTo(1))
                .body("response.totalPageCount", notNullValue())
                .body("response.totalElementCount", notNullValue())
                .body("response.sortedBy", equalTo(null))
                .body("response.filteredBy.statuses", hasItem("AVAILABLE"))
                .body("response.filteredBy.phoneNumber", equalTo(null));
    }

    @Test()
    @Story("As an admin I want to get proper error message when I request to filter assignments with invalid status input")
    @Severity(SeverityLevel.NORMAL)
    @Description("Prior to executing this method, an assignment is created to prevent failures in case no assignment is associated with the institution.")
    public void listAssignmentsWithInvalidStatus() {
        Helper.createANewAssignment();
        requestBodyAssignments.setPagination(Helper.createPagination());
        requestBodyAssignments.setFilter(Helper.createFilterWithAssignmentStatus("available", "ASSIGNED"));
        Response response = InstitutionEndpoints.listAssignments(requestBodyAssignments);
        response.then()
                .spec(badRequestResponseSpec());

    }

    @Test()
    @Story("As an admin I want to list assignments with valid status and linenumber and without country code")
    @Severity(SeverityLevel.NORMAL)
    @Description("Prior to executing this method, an assignment is created to prevent failures in case no assignment is associated with the institution.")
    public void listAssignmentsWithValidStatusAndLineNumber() {
        Assignment assignment = Helper.createANewAssignment();
        requestBodyAssignments.setPagination(Helper.createPagination());
        requestBodyAssignments.setFilter(Helper.createFilterWithAssignmentStatusAndLineNumber(assignment.getPhoneNumber().getLineNumber(), "AVAILABLE"));
        Response response = InstitutionEndpoints.listAssignments(requestBodyAssignments);
        response.then()
                .spec(successResponseSpec())
                .spec(listAssignmentsResponseSpec(assignment))
                .body("response.filteredBy.statuses", hasItem("AVAILABLE"))
                .body("response.filteredBy.phoneNumber.lineNumber", equalTo(assignment.getPhoneNumber().getLineNumber()))
                .body("response.filteredBy.phoneNumber.countryCode", equalTo(null));

    }

    @Test()
    @Story("As an admin I want to list assignments with valid status and country code and without line number")
    @Severity(SeverityLevel.NORMAL)
    @Description("Prior to executing this method, an assignment is created to prevent failures in case no assignment is associated with the institution.")
    public void listAssignmentsWithValidStatusAndCountryCode() {
        Assignment assignment = Helper.createANewAssignment();
        requestBodyAssignments.setPagination(Helper.createPagination());
        requestBodyAssignments.setFilter(Helper.createFilterWithAssignmentStatusAndCountryCOde(assignment.getPhoneNumber().getCountryCode(), "AVAILABLE"));
        Response response = InstitutionEndpoints.listAssignments(requestBodyAssignments);
        response.then()
                .body("response.content[0].createdAt", notNullValue())
                .body("response.content[0].createdUser", notNullValue())
                .body("response.content[0].id", notNullValue())
                .body("response.content[0].status", notNullValue())
                .body("response.content[0].description", notNullValue())
                .body("response.content[0].firstName", notNullValue())
                .body("response.content[0].lastName", notNullValue())
                .body("response.content[0].location", notNullValue())
                .body("response.pageNumber", equalTo(1))
                .body("response.totalPageCount", notNullValue())
                .body("response.totalElementCount", notNullValue())
                .body("response.sortedBy", equalTo(null))
                .body("response.filteredBy.statuses", hasItem("AVAILABLE"))
                .body("response.filteredBy.phoneNumber.lineNumber", equalTo(null))
                .body("response.filteredBy.phoneNumber.countryCode", equalTo(assignment.getPhoneNumber().getCountryCode()));

    }

    @Test()
    @Story("As an admin I want to list assignments with valid status,linenumber,country code")
    @Severity(SeverityLevel.NORMAL)
    @Description("Prior to executing this method, an assignment is created to prevent failures in case no assignment is associated with the institution.")
    public void listAssignmentsWithValidStatusAndPhoneNumber() {
        Assignment assignment = Helper.createANewAssignment();
        requestBodyAssignments.setPagination(Helper.createPagination());
        requestBodyAssignments.setFilter(Helper.createFilterWithAssignmentStatusPhoneNumber(assignment.getPhoneNumber(), "AVAILABLE"));
        Response response = InstitutionEndpoints.listAssignments(requestBodyAssignments);
        response.then()
                .spec(successResponseSpec())
                .spec(listAssignmentsResponseSpec(assignment))
                .body("response.filteredBy.statuses", hasItem("AVAILABLE"))
                .body("response.filteredBy.phoneNumber.lineNumber", equalTo(assignment.getPhoneNumber().getLineNumber()))
                .body("response.filteredBy.phoneNumber.countryCode", equalTo(assignment.getPhoneNumber().getCountryCode()));

    }


    private ResponseSpecification successResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType("application/json")
                .expectBody("time", notNullValue())
                .expectBody("httpStatus", equalTo("OK"))
                .expectBody("isSuccess", equalTo(true))
                .build();
    }

    private ResponseSpecification badRequestResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(400)
                .expectContentType("application/json")
                .expectBody("time", notNullValue())
                .expectBody("httpStatus", equalTo("BAD_REQUEST"))
                .expectBody("header", equalTo("VALIDATION ERROR"))
                .expectBody("isSuccess", equalTo(false))
                .build();
    }

    private ResponseSpecification listAssignmentsResponseSpec(Assignment assignment) {
        return new ResponseSpecBuilder()
                .expectBody("response.content", instanceOf(List.class))
                .expectBody("response.content[0].createdAt", notNullValue())
                .expectBody("response.content[0].createdUser", notNullValue())
                .expectBody("response.content[0].id", notNullValue())
                .expectBody("response.content[0].status", notNullValue())
                .expectBody("response.content[0].description", equalTo(assignment.getDescription()))
                .expectBody("response.content[0].firstName", equalTo(assignment.getFirstName()))
                .expectBody("response.content[0].lastName", equalTo(assignment.getLastName()))
                .expectBody("response.content[0].location", notNullValue())
                .expectBody("response.pageNumber", equalTo(1))
                .expectBody("response.totalPageCount", notNullValue())
                .expectBody("response.totalElementCount", notNullValue())
                .expectBody("response.sortedBy", equalTo(null))
                .build();
    }

}
