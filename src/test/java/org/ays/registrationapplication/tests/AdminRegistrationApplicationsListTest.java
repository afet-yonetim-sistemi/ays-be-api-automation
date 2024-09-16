package org.ays.registrationapplication.tests;

import io.restassured.response.Response;
import org.ays.common.model.payload.AysOrder;
import org.ays.common.util.AysDataProvider;
import org.ays.common.util.AysResponseSpecs;
import org.ays.registrationapplication.endpoints.AdminRegistrationApplicationEndpoints;
import org.ays.registrationapplication.model.enums.AdminRegistrationApplicationStatus;
import org.ays.registrationapplication.model.payload.AdminRegistrationApplicationListPayload;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class AdminRegistrationApplicationsListTest {

    @Test(groups = {"Smoke", "Regression", "SuperAdmin"})
    public void postRegistrationApplicationsWithPagination() {
        AdminRegistrationApplicationListPayload applicationListPayload = AdminRegistrationApplicationListPayload
                .generate();
        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplications(applicationListPayload);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .body("response.content", notNullValue())
                .body("response.sortedBy", nullValue());
    }

    @Test(groups = {"Regression", "SuperAdmin"}, dataProvider = "negativePageableData",dataProviderClass = AysDataProvider.class)
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
    public void postRegistrationApplicationsWithPaginationAndFilterNegative() {
        AdminRegistrationApplicationListPayload applicationListPayload = AdminRegistrationApplicationListPayload
                .generate();
        applicationListPayload.getFilter().setStatuses(List.of(AdminRegistrationApplicationStatus.WAITING));
        Response response = AdminRegistrationApplicationEndpoints.postRegistrationApplications(applicationListPayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec());
    }

    @Test(groups = {"Smoke", "Regression", "SuperAdmin"})
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