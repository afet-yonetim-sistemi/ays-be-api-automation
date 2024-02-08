package tests.institution.adminregistrationmanagementservice;

import endpoints.InstitutionEndpoints;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import payload.Filter;
import payload.Pagination;
import payload.RequestBodyInstitution;
import payload.Sort;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;

public class PostAdminRegistrationApplicationsTest {
    RequestBodyInstitution requestBodyInstitution;
    Pagination pagination;
    Filter filter;
    Sort sort;

    @BeforeMethod
    public void setup() {
        requestBodyInstitution = new RequestBodyInstitution();
        pagination = new Pagination();
        filter = new Filter();
        sort = new Sort();
    }

    @Test()
    @Story("As a super admin I want to list administrator registration applications with request to pagination")
    @Severity(SeverityLevel.NORMAL)
    public void postRegistrationApplicationsWithPagination() {
        pagination.setPage(1);
        pagination.setPageSize(10);
        requestBodyInstitution.setPagination(pagination);


        Response response = InstitutionEndpoints.postRegistrationApplications(requestBodyInstitution);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("response.content", notNullValue())
                .body("response.sortedBy", nullValue());
    }

    @Test(dataProvider = "paginationScenarios")
    @Story("As a super admin I want to get proper error message when I request to pagination with invalid data")
    @Severity(SeverityLevel.NORMAL)
    public void postRegistrationApplicationsWithPaginationNegative(int page, int pageSize) {
        pagination.setPage(page);
        pagination.setPageSize(pageSize);
        requestBodyInstitution.setPagination(pagination);


        Response response = InstitutionEndpoints.postRegistrationApplications(requestBodyInstitution);
        response.then()
                .statusCode(400)
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("isSuccess", equalTo(false))
                .body("header", equalTo("VALIDATION ERROR"))
                .body("subErrors[0].message", containsString("must be between 1 and 99999999"));
    }

    @Test()
    @Story("As a super admin I want to list administrator registration applications with request to pagination and filter")
    @Severity(SeverityLevel.NORMAL)
    public void postRegistrationApplicationsWithPaginationAndFilter() {
        pagination.setPage(1);
        pagination.setPageSize(10);
        requestBodyInstitution.setPagination(pagination);

        List<String> newStatuses = new ArrayList<>();
        newStatuses.add("WAITING");
        filter.setStatuses(newStatuses);
        requestBodyInstitution.setFilter(filter);

        Response response = InstitutionEndpoints.postRegistrationApplications(requestBodyInstitution);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("response.content", notNullValue())
                .body("response.sortedBy", nullValue())
                .body("response.filteredBy", notNullValue());
    }

    @Test()
    @Story("As a super admin I want to get proper error message when I request to filter with invalid status input")
    @Severity(SeverityLevel.NORMAL)
    @Description("Pagination and filter with invalid statuses value")
    public void postRegistrationApplicationsWithPaginationAndFilterNegative() {
        pagination.setPage(1);
        pagination.setPageSize(10);
        requestBodyInstitution.setPagination(pagination);
        List<String> statuses = new ArrayList<>();
        statuses.add("WAIT");
        filter.setStatuses(statuses);
        requestBodyInstitution.setFilter(filter);
        Response response = InstitutionEndpoints.postRegistrationApplications(requestBodyInstitution);
        response.then()
                .statusCode(400)
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("isSuccess", equalTo(false))
                .body("header", equalTo("VALIDATION ERROR"));
    }

    @Test()
    @Story("As a super admin I want to list administrator registration applications with request to pagination and sort")
    @Severity(SeverityLevel.NORMAL)
    @Description("Pagination and sort with Authorization")
    public void postRegistrationApplicationsWithPaginationAndSort() {
        pagination.setPage(1);
        pagination.setPageSize(10);
        requestBodyInstitution.setPagination(pagination);

        sort.setProperty("createdAt");
        sort.setDirection("ASC");
        List<Sort> newSort = new ArrayList<>();
        newSort.add(sort);
        requestBodyInstitution.setSort(newSort);

        Response response = InstitutionEndpoints.postRegistrationApplications(requestBodyInstitution);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("response.content", notNullValue())
                .body("response.sortedBy", notNullValue())
                .body("response.filteredBy", nullValue());
    }

    @Test()
    @Story("As a super admin I want to get proper error message when I request to sort with invalid input")
    @Severity(SeverityLevel.NORMAL)
    @Description("Pagination and invalid sort value with Authorization")
    public void postRegistrationApplicationsWithPaginationAndInvalidSort() {
        pagination.setPage(1);
        pagination.setPageSize(10);
        requestBodyInstitution.setPagination(pagination);

        sort.setProperty("created");
        sort.setDirection("ASC");
        List<Sort> newSort = new ArrayList<>();
        newSort.add(sort);
        requestBodyInstitution.setSort(newSort);

        Response response = InstitutionEndpoints.postRegistrationApplications(requestBodyInstitution);
        response.then()
                .statusCode(400)
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("isSuccess", equalTo(false))
                .body("header", equalTo("VALIDATION ERROR"));
    }

    @Test()
    @Story("As a super admin I want to list administrator registration applications with request to pagination filter and sort")
    @Severity(SeverityLevel.NORMAL)
    @Description("Pagination,sort and filter with Authorization")
    public void postRegistrationApplicationsWithPaginationSortFilter() {
        pagination.setPage(1);
        pagination.setPageSize(10);
        requestBodyInstitution.setPagination(pagination);

        List<String> statuses = new ArrayList<>();
        statuses.add("WAITING");
        filter.setStatuses(statuses);
        requestBodyInstitution.setFilter(filter);

        sort.setProperty("createdAt");
        sort.setDirection("ASC");
        List<Sort> newSort = new ArrayList<>();
        newSort.add(sort);
        requestBodyInstitution.setSort(newSort);

        Response response = InstitutionEndpoints.postRegistrationApplications(requestBodyInstitution);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("response.content", notNullValue())
                .body("response.sortedBy", notNullValue())
                .body("response.filteredBy", notNullValue());
    }

    @Test()
    @Story("As a super admin I want to get proper error message when I request to sort and filter with invalid input")
    @Severity(SeverityLevel.NORMAL)
    @Description("Pagination,invalid sort and invalid filter with Authorization")
    public void postRegistrationApplicationsWithPaginationInvalidSortInvalidFilter() {
        pagination.setPage(1);
        pagination.setPageSize(10);
        requestBodyInstitution.setPagination(pagination);

        List<String> statuses = new ArrayList<>();
        statuses.add("WAIT");
        filter.setStatuses(statuses);
        requestBodyInstitution.setFilter(filter);

        sort.setProperty("");
        sort.setDirection("ASC");
        List<Sort> newSort = new ArrayList<>();
        newSort.add(sort);
        requestBodyInstitution.setSort(newSort);

        Response response = InstitutionEndpoints.postRegistrationApplications(requestBodyInstitution);
        response.then()
                .statusCode(400)
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("isSuccess", equalTo(false))
                .body("header", equalTo("VALIDATION ERROR"));
    }


    @DataProvider(name = "paginationScenarios") //ARMS_03, ARMS_04, ARMS_05
    public Object[][] paginationScenarios() {
        return new Object[][]{
                {0, 10},
                {1, 0},
                {-1, 10},
                {1, -1},
                {-1, -1}
        };

    }
}