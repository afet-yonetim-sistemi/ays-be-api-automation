package org.ays.registrationapplication.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.ays.common.model.payload.AysOrder;
import org.ays.common.util.AysResponseSpecs;
import org.ays.registrationapplication.endpoints.AdminRegistrationApplicationEndpoints;
import org.ays.registrationapplication.model.enums.AdminRegistrationApplicationStatus;
import org.ays.registrationapplication.model.payload.AdminRegistrationApplicationListPayload;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class PostAdminRegistrationApplicationsTest {

    @Test(groups = {"Smoke", "Regression", "SuperAdmin"})
    @Story("As a super admin I want to list administrator registration applications with request to pagination")
    @Severity(SeverityLevel.NORMAL)
    public void postRegistrationApplicationsWithPagination() {
        AdminRegistrationApplicationListPayload applicationListPayload = AdminRegistrationApplicationListPayload
                .generate();
        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplications(applicationListPayload);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .body("response.content", notNullValue())
                .body("response.sortedBy", nullValue());
    }

    @Test(groups = {"Regression", "SuperAdmin"}, dataProvider = "paginationScenarios")
    @Story("As a super admin I want to get proper error message when I request to pagination with invalid data")
    @Severity(SeverityLevel.NORMAL)
    public void postRegistrationApplicationsWithPaginationNegative(int page, int pageSize) {
        AdminRegistrationApplicationListPayload applicationListPayload = AdminRegistrationApplicationListPayload
                .generate();
        applicationListPayload.getPageable().setPage(page);
        applicationListPayload.getPageable().setPageSize(pageSize);
        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplications(applicationListPayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", containsString("must be between 1 and 99999999"));
    }

    @Test(groups = {"Smoke", "Regression", "SuperAdmin"})
    @Story("As a super admin I want to list administrator registration applications with request to pagination and filter")
    @Severity(SeverityLevel.NORMAL)
    public void postRegistrationApplicationsWithPaginationAndFilter() {
        AdminRegistrationApplicationListPayload applicationListPayload = AdminRegistrationApplicationListPayload
                .generate();
        applicationListPayload.getFilter().setStatuses(List.of(AdminRegistrationApplicationStatus.WAITING));
        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplications(applicationListPayload);
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
        AdminRegistrationApplicationListPayload applicationListPayload = AdminRegistrationApplicationListPayload
                .generate();
        applicationListPayload.getFilter().setStatuses(List.of(AdminRegistrationApplicationStatus.WAITING));
        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplications(applicationListPayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec());
    }

    @Test(groups = {"Smoke", "Regression", "SuperAdmin"})
    @Story("As a super admin I want to list administrator registration applications with request to pagination and sort")
    @Severity(SeverityLevel.NORMAL)
    @Description("Pagination and sort with Authorization")
    public void postRegistrationApplicationsWithPaginationAndSort() {
        AdminRegistrationApplicationListPayload applicationListPayload = AdminRegistrationApplicationListPayload
                .generate();
        List<AysOrder> orders = AysOrder.generate("createdAt", "ASC");
        applicationListPayload.getPageable().setOrders(orders);
        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplications(applicationListPayload);
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
        AdminRegistrationApplicationListPayload applicationListPayload = AdminRegistrationApplicationListPayload
                .generate();
        List<AysOrder> orders = AysOrder.generate("createdAt", "ASC");
        applicationListPayload.getPageable().setOrders(orders);
        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplications(applicationListPayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec());
    }

    @Test(groups = {"Smoke", "Regression", "SuperAdmin"})
    @Story("As a super admin I want to list administrator registration applications with request to pagination filter and sort")
    @Severity(SeverityLevel.NORMAL)
    @Description("Pagination,sort and filter with Authorization")
    public void postRegistrationApplicationsWithPaginationSortFilter() {
        AdminRegistrationApplicationListPayload applicationListPayload = AdminRegistrationApplicationListPayload
                .generate();
        applicationListPayload.getFilter().setStatuses(List.of(AdminRegistrationApplicationStatus.WAITING));
        List<AysOrder> orders = AysOrder.generate("createdAt", "ASC");
        applicationListPayload.getPageable().setOrders(orders);
        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplications(applicationListPayload);
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
        AdminRegistrationApplicationListPayload applicationListPayload = AdminRegistrationApplicationListPayload
                .generate();
        applicationListPayload.getFilter().setStatuses(List.of(AdminRegistrationApplicationStatus.WAITING));
        List<AysOrder> orders = AysOrder.generate("", "ASC");
        applicationListPayload.getPageable().setOrders(orders);
        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplications(applicationListPayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec());
    }

}