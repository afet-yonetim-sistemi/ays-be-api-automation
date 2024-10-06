package org.ays.registrationapplication.tests;

import io.restassured.response.Response;
import org.ays.common.model.payload.AysOrder;
import org.ays.common.util.AysDataProvider;
import org.ays.common.util.AysResponseSpecs;
import org.ays.registrationapplication.endpoints.AdminRegistrationApplicationEndpoints;
import org.ays.registrationapplication.model.enums.AdminRegistrationApplicationStatus;
import org.ays.registrationapplication.model.payload.AdminRegistrationApplicationListPayload;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class AdminRegistrationApplicationsListTest {

    @Test(groups = {"Smoke", "Regression", "SuperAdmin"})
    public void postRegistrationApplicationsWithPagination() {

        AdminRegistrationApplicationListPayload listPayload = AdminRegistrationApplicationListPayload.generate();

        Response response = AdminRegistrationApplicationEndpoints.findAll(listPayload);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .body("response.content", notNullValue())
                .body("response.sortedBy", nullValue());
    }


    @Test(groups = {"Regression", "SuperAdmin"}, dataProvider = "negativePageableData", dataProviderClass = AysDataProvider.class)
    public void postRegistrationApplicationsWithPaginationNegative(int page, int pageSize) {

        AdminRegistrationApplicationListPayload listPayload = AdminRegistrationApplicationListPayload.generate();

        listPayload.getPageable().setPage(page);
        listPayload.getPageable().setPageSize(pageSize);

        Response response = AdminRegistrationApplicationEndpoints.findAll(listPayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", containsString("must be between 1 and 99999999"));
    }


    @Test(groups = {"Smoke", "Regression", "SuperAdmin"})
    public void postRegistrationApplicationsWithPaginationAndFilter() {

        AdminRegistrationApplicationListPayload listPayload = AdminRegistrationApplicationListPayload.generate();

        listPayload.getFilter().setStatuses(List.of(AdminRegistrationApplicationStatus.WAITING));

        Response response = AdminRegistrationApplicationEndpoints.findAll(listPayload);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .body("response.content", notNullValue())
                .body("response.sortedBy", nullValue())
                .body("response.filteredBy", notNullValue());
    }


    @Test(groups = {"Regression", "SuperAdmin"})
    @Ignore("Testi anlamadığım için şimdilik ignore ettim.")
    public void postRegistrationApplicationsWithPaginationAndFilterNegative() {

        AdminRegistrationApplicationListPayload listPayload = AdminRegistrationApplicationListPayload.generate();
        listPayload.getFilter().setStatuses(List.of(AdminRegistrationApplicationStatus.WAITING));

        Response response = AdminRegistrationApplicationEndpoints.findAll(listPayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec());
    }


    @Test(groups = {"Smoke", "Regression", "SuperAdmin"})
    public void postRegistrationApplicationsWithPaginationAndSort() {

        AdminRegistrationApplicationListPayload listPayload = AdminRegistrationApplicationListPayload.generate();

        List<AysOrder> orders = AysOrder.generate("createdAt", "ASC");
        listPayload.getPageable().setOrders(orders);
        listPayload.setFilter(null);

        Response response = AdminRegistrationApplicationEndpoints.findAll(listPayload);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .body("response.content", notNullValue())
                .body("response.orderedBy", notNullValue())
                .body("response.filteredBy", nullValue());
    }


    @Test(groups = {"Regression", "SuperAdmin"})
    public void postRegistrationApplicationsWithPaginationAndInvalidSort() {

        AdminRegistrationApplicationListPayload listPayload = AdminRegistrationApplicationListPayload.generate();
        List<AysOrder> orders = AysOrder.generate("name", "ASC");
        listPayload.getPageable().setOrders(orders);

        Response response = AdminRegistrationApplicationEndpoints.findAll(listPayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec());
    }

    @Test(groups = {"Smoke", "Regression", "SuperAdmin"})
    public void postRegistrationApplicationsWithPaginationSortFilter() {

        AdminRegistrationApplicationListPayload listPayload = AdminRegistrationApplicationListPayload.generate();

        List<AysOrder> orders = AysOrder.generate("createdAt", "ASC");
        listPayload.getPageable().setOrders(orders);
        listPayload.getFilter().setStatuses(List.of(AdminRegistrationApplicationStatus.WAITING));

        Response response = AdminRegistrationApplicationEndpoints.findAll(listPayload);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .body("response.content", notNullValue())
                .body("response.orderedBy", notNullValue())
                .body("response.filteredBy", notNullValue());
    }

    @Test(groups = {"Regression", "SuperAdmin"})
    public void postRegistrationApplicationsWithPaginationInvalidSortInvalidFilter() {
        AdminRegistrationApplicationListPayload listPayload = AdminRegistrationApplicationListPayload.generate();

        listPayload.getFilter().setStatuses(List.of(AdminRegistrationApplicationStatus.WAITING));
        List<AysOrder> orders = AysOrder.generate("", "ASC");
        listPayload.getPageable().setOrders(orders);

        Response response = AdminRegistrationApplicationEndpoints.findAll(listPayload);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec());
    }

}