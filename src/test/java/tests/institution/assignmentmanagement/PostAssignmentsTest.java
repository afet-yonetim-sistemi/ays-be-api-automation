package tests.institution.assignmentmanagement;


import endpoints.InstitutionEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import payload.FiltersForAssignments;
import payload.Pagination;
import payload.PhoneNumber;
import payload.RequestBodyAssignments;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.nullValue;

public class PostAssignmentsTest extends InstitutionEndpoints{
    RequestBodyAssignments requestBodyAssignments=new RequestBodyAssignments();
    PhoneNumber phoneNumber = new PhoneNumber();
    @BeforeMethod
    public void setUp() {
        requestBodyAssignments = new RequestBodyAssignments();
        phoneNumber = new PhoneNumber();
    }


    @Test
    public void listAssignmentsByNoFilters(){
        requestBodyAssignments.setPagination(new Pagination());
        requestBodyAssignments.getPagination().setPage(1);
        requestBodyAssignments.getPagination().setPageSize(10);
        Response response= InstitutionEndpoints.listAssignments(requestBodyAssignments);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("response.content", notNullValue())
                .body("response.sortedBy", nullValue())
                .body("response.filteredBy", nullValue());
    }
    @Test
    public void listAssignmentsWithAllFilters(){
        requestBodyAssignments.setPagination(new Pagination());
        requestBodyAssignments.getPagination().setPage(1);
        requestBodyAssignments.getPagination().setPageSize(10);
        requestBodyAssignments.setFilter(new FiltersForAssignments());
        requestBodyAssignments.getFilter().setStatuses(Arrays.asList("AVAILABLE"));
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setCountryCode("90");
        phoneNumber.setLineNumber("1234567890");
        requestBodyAssignments.getFilter().setPhoneNumber(phoneNumber);
        Response response=InstitutionEndpoints.listAssignments(requestBodyAssignments);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("response.content", notNullValue())
                .body("response.sortedBy", nullValue())
                .body("response.filteredBy.statuses", equalTo(requestBodyAssignments.getFilter().getStatuses()))
                .body("response.filteredBy.phoneNumber.countryCode", equalTo(requestBodyAssignments.getFilter().getPhoneNumber().getCountryCode()))
                .body("response.filteredBy.phoneNumber.lineNumber", equalTo(requestBodyAssignments.getFilter().getPhoneNumber().getLineNumber()));
    }
    @Test
    public void filterAssignmentsByWithoutStatus(){
        requestBodyAssignments.setPagination(new Pagination());
        requestBodyAssignments.getPagination().setPage(1);
        requestBodyAssignments.getPagination().setPageSize(10);
        requestBodyAssignments.setFilter(new FiltersForAssignments());
        phoneNumber.setCountryCode("90");
        phoneNumber.setLineNumber("1234567890");
        requestBodyAssignments.getFilter().setPhoneNumber(phoneNumber);
        Response response=InstitutionEndpoints.listAssignments(requestBodyAssignments);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("response.content", notNullValue())
                .body("response.sortedBy", nullValue())
                .body("response.filteredBy.statuses", nullValue());
    }
    @Test
    public void filterAssignmentsByWithoutCountryCode(){
        requestBodyAssignments.setPagination(new Pagination());
        requestBodyAssignments.getPagination().setPage(1);
        requestBodyAssignments.getPagination().setPageSize(10);

        requestBodyAssignments.setFilter(new FiltersForAssignments());
        requestBodyAssignments.getFilter().setStatuses(Arrays.asList("AVAILABLE"));

        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setLineNumber("1234567890");
        requestBodyAssignments.getFilter().setPhoneNumber(phoneNumber);
        Response response=InstitutionEndpoints.listAssignments(requestBodyAssignments);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("response.content", notNullValue())
                .body("response.sortedBy", nullValue())
                .body("response.filteredBy.statuses", equalTo(requestBodyAssignments.getFilter().getStatuses()))
                .body("response.filteredBy.phoneNumber.countryCode",nullValue())
                .body("response.filteredBy.phoneNumber.lineNumber", equalTo(requestBodyAssignments.getFilter().getPhoneNumber().getLineNumber()));
    }
    @Test
    public void filterAssignmentsByWithoutLineNumber(){
        requestBodyAssignments.setPagination(new Pagination());
        requestBodyAssignments.getPagination().setPage(1);
        requestBodyAssignments.getPagination().setPageSize(10);

        requestBodyAssignments.setFilter(new FiltersForAssignments());
        requestBodyAssignments.getFilter().setStatuses(Arrays.asList("AVAILABLE"));

        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setCountryCode("90");
        requestBodyAssignments.getFilter().setPhoneNumber(phoneNumber);
        Response response=InstitutionEndpoints.listAssignments(requestBodyAssignments);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("response.content", notNullValue())
                .body("response.sortedBy", nullValue())
                .body("response.filteredBy.statuses", equalTo(requestBodyAssignments.getFilter().getStatuses()))
                .body("response.filteredBy.phoneNumber.countryCode",equalTo(requestBodyAssignments.getFilter().getPhoneNumber().getCountryCode()))
                .body("response.filteredBy.phoneNumber.lineNumber", nullValue());
    }
    @Test
    public void filterAssignmentsByWithoutPhoneNumber(){
       requestBodyAssignments.setPagination(new Pagination());
        requestBodyAssignments.getPagination().setPage(1);
        requestBodyAssignments.getPagination().setPageSize(10);

       requestBodyAssignments.setFilter(new FiltersForAssignments());
        requestBodyAssignments.getFilter().setStatuses(Arrays.asList("AVAILABLE"));

        Response response=InstitutionEndpoints.listAssignments(requestBodyAssignments);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("response.content", notNullValue())
                .body("response.sortedBy", nullValue())
                .body("response.filteredBy.statuses", equalTo(requestBodyAssignments.getFilter().getStatuses()))
                .body("response.filteredBy.phoneNumber",nullValue());
    }
    @Test
    public void filterAssignmentsWithMultipleStatuses(){
        requestBodyAssignments.setPagination(new Pagination());
        requestBodyAssignments.getPagination().setPage(1);
        requestBodyAssignments.getPagination().setPageSize(10);
        requestBodyAssignments.setFilter(new FiltersForAssignments());
        requestBodyAssignments.getFilter().setStatuses(Arrays.asList("AVAILABLE","IN_PROGRESS"));
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setCountryCode("90");
        phoneNumber.setLineNumber("1234567890");
        requestBodyAssignments.getFilter().setPhoneNumber(phoneNumber);
        Response response=InstitutionEndpoints.listAssignments(requestBodyAssignments);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("response.content", notNullValue())
                .body("response.sortedBy", nullValue())
                .body("response.filteredBy.statuses", equalTo(requestBodyAssignments.getFilter().getStatuses()))
                .body("response.filteredBy.phoneNumber.countryCode", equalTo(requestBodyAssignments.getFilter().getPhoneNumber().getCountryCode()))
                .body("response.filteredBy.phoneNumber.lineNumber", equalTo(requestBodyAssignments.getFilter().getPhoneNumber().getLineNumber()));
    }




}
