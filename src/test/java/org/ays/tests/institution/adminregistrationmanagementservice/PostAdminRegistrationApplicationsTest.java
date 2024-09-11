package org.ays.tests.institution.adminregistrationmanagementservice;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.ays.payload.AysPageable;
import org.ays.payload.Filter;
import org.ays.payload.Orders;
import org.ays.payload.RequestBodyInstitution;
import org.ays.registrationapplication.endpoints.AdminRegistrationApplicationEndpoints;
import org.ays.utility.AysResponseSpecs;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class PostAdminRegistrationApplicationsTest {
    RequestBodyInstitution requestBodyInstitution;
    AysPageable pageable;
    Filter filter;
    Orders orders;

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        requestBodyInstitution = new RequestBodyInstitution();
        pageable = new AysPageable();
        filter = new Filter();
        orders = new Orders();
    }

    @Test(groups = {"Smoke", "Regression", "SuperAdmin"})
    @Story("As a super admin I want to list administrator registration applications with request to pagination")
    @Severity(SeverityLevel.NORMAL)
    public void postRegistrationApplicationsWithPagination() {
        pageable.setPage(1);
        pageable.setPageSize(10);
        requestBodyInstitution.setPageable(pageable);


        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplications(requestBodyInstitution);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .body("response.content", notNullValue())
                .body("response.sortedBy", nullValue());
    }

    @Test(groups = {"Regression", "SuperAdmin"}, dataProvider = "paginationScenarios")
    @Story("As a super admin I want to get proper error message when I request to pagination with invalid data")
    @Severity(SeverityLevel.NORMAL)
    public void postRegistrationApplicationsWithPaginationNegative(int page, int pageSize) {
        pageable.setPage(page);
        pageable.setPageSize(pageSize);
        requestBodyInstitution.setPageable(pageable);


        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplications(requestBodyInstitution);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", containsString("must be between 1 and 99999999"));
    }

    @Test(groups = {"Smoke", "Regression", "SuperAdmin"})
    @Story("As a super admin I want to list administrator registration applications with request to pagination and filter")
    @Severity(SeverityLevel.NORMAL)
    public void postRegistrationApplicationsWithPaginationAndFilter() {
        pageable.setPage(1);
        pageable.setPageSize(10);
        requestBodyInstitution.setPageable(pageable);

        List<String> newStatuses = new ArrayList<>();
        newStatuses.add("WAITING");
        filter.setStatuses(newStatuses);
        requestBodyInstitution.setFilter(filter);

        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplications(requestBodyInstitution);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .body("response.content", notNullValue())
                .body("response.sortedBy", nullValue())
                .body("response.filteredBy", notNullValue());
    }

    @Test(groups = {"Regression", "SuperAdmin"})
    @Story("As a super admin I want to get proper error message when I request to filter with invalid status input")
    @Severity(SeverityLevel.NORMAL)
    @Description("Pagination and filter with invalid statuses value")
    public void postRegistrationApplicationsWithPaginationAndFilterNegative() {
        pageable.setPage(1);
        pageable.setPageSize(10);
        requestBodyInstitution.setPageable(pageable);
        List<String> statuses = new ArrayList<>();
        statuses.add("WAIT");
        filter.setStatuses(statuses);
        requestBodyInstitution.setFilter(filter);
        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplications(requestBodyInstitution);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec());
    }

    @Test(groups = {"Smoke", "Regression", "SuperAdmin"})
    @Story("As a super admin I want to list administrator registration applications with request to pagination and sort")
    @Severity(SeverityLevel.NORMAL)
    @Description("Pagination and sort with Authorization")
    public void postRegistrationApplicationsWithPaginationAndSort() {
        pageable.setPage(1);
        pageable.setPageSize(10);
        requestBodyInstitution.setPageable(pageable);

        orders.setProperty("createdAt");
        orders.setDirection("ASC");
        List<Orders> newOrders = new ArrayList<>();
        newOrders.add(orders);
        requestBodyInstitution.setOrders(newOrders);

        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplications(requestBodyInstitution);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .body("response.content", notNullValue())
                .body("response.sortedBy", notNullValue())
                .body("response.filteredBy", nullValue());
    }

    @Test(groups = {"Regression", "SuperAdmin"})
    @Story("As a super admin I want to get proper error message when I request to sort with invalid input")
    @Severity(SeverityLevel.NORMAL)
    @Description("Pagination and invalid sort value with Authorization")
    public void postRegistrationApplicationsWithPaginationAndInvalidSort() {
        pageable.setPage(1);
        pageable.setPageSize(10);
        requestBodyInstitution.setPageable(pageable);

        orders.setProperty("created");
        orders.setDirection("ASC");
        List<Orders> newOrders = new ArrayList<>();
        newOrders.add(orders);
        requestBodyInstitution.setOrders(newOrders);

        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplications(requestBodyInstitution);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec());
    }

    @Test(groups = {"Smoke", "Regression", "SuperAdmin"})
    @Story("As a super admin I want to list administrator registration applications with request to pagination filter and sort")
    @Severity(SeverityLevel.NORMAL)
    @Description("Pagination,sort and filter with Authorization")
    public void postRegistrationApplicationsWithPaginationSortFilter() {
        pageable.setPage(1);
        pageable.setPageSize(10);
        requestBodyInstitution.setPageable(pageable);

        List<String> statuses = new ArrayList<>();
        statuses.add("WAITING");
        filter.setStatuses(statuses);
        requestBodyInstitution.setFilter(filter);

        orders.setProperty("createdAt");
        orders.setDirection("ASC");
        List<Orders> newOrders = new ArrayList<>();
        newOrders.add(orders);
        requestBodyInstitution.setOrders(newOrders);

        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplications(requestBodyInstitution);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .body("response.content", notNullValue())
                .body("response.sortedBy", notNullValue())
                .body("response.filteredBy", notNullValue());
    }

    @Test(groups = {"Regression", "SuperAdmin"})
    @Story("As a super admin I want to get proper error message when I request to sort and filter with invalid input")
    @Severity(SeverityLevel.NORMAL)
    @Description("Pagination,invalid sort and invalid filter with Authorization")
    public void postRegistrationApplicationsWithPaginationInvalidSortInvalidFilter() {
        pageable.setPage(1);
        pageable.setPageSize(10);
        requestBodyInstitution.setPageable(pageable);

        List<String> statuses = new ArrayList<>();
        statuses.add("WAIT");
        filter.setStatuses(statuses);
        requestBodyInstitution.setFilter(filter);

        orders.setProperty("");
        orders.setDirection("ASC");
        List<Orders> newOrders = new ArrayList<>();
        newOrders.add(orders);
        requestBodyInstitution.setOrders(newOrders);

        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplications(requestBodyInstitution);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec());
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