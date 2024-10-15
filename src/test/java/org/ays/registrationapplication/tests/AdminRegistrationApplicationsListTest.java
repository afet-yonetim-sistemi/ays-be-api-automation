package org.ays.registrationapplication.tests;

import io.restassured.response.Response;
import org.ays.auth.endpoints.AuthEndpoints;
import org.ays.auth.payload.LoginPayload;
import org.ays.common.model.enums.AysErrorMessage;
import org.ays.common.model.payload.AysOrder;
import org.ays.common.model.payload.AysPageable;
import org.ays.common.util.AysDataProvider;
import org.ays.common.util.AysResponseSpecs;
import org.ays.registrationapplication.endpoints.AdminRegistrationApplicationEndpoints;
import org.ays.registrationapplication.model.enums.AdminRegistrationApplicationStatus;
import org.ays.registrationapplication.model.payload.AdminRegistrationApplicationListPayload;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class AdminRegistrationApplicationsListTest {

    @Test(groups = {"Smoke", "Regression"})
    public void listRegistrationApplicationsWithPageable() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        AdminRegistrationApplicationListPayload listPayload = AdminRegistrationApplicationListPayload.generate();

        Response response = AdminRegistrationApplicationEndpoints.findAll(listPayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .body("response.content", notNullValue())
                .body("response.sortedBy", nullValue());
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidPageableData", dataProviderClass = AysDataProvider.class)
    public void listRegistrationApplicationsWithInvalidPageableData(int page, int pageSize, AysErrorMessage errorMessage, String field, String type ) {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        AdminRegistrationApplicationListPayload listPayload = AdminRegistrationApplicationListPayload.generate();

        listPayload.getPageable().setPage(page);
        listPayload.getPageable().setPageSize(pageSize);

        Response response = AdminRegistrationApplicationEndpoints.findAll(listPayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Smoke", "Regression"})
    public void listRegistrationApplicationsWithPageableAndFilter() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        AdminRegistrationApplicationListPayload listPayload = AdminRegistrationApplicationListPayload.generate();

        listPayload.getFilter().setStatuses(List.of(AdminRegistrationApplicationStatus.WAITING));

        Response response = AdminRegistrationApplicationEndpoints.findAll(listPayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .body("response.content", notNullValue())
                .body("response.sortedBy", nullValue())
                .body("response.filteredBy", notNullValue());
    }

    @Test(groups = {"Regression"})
    public void listRegistrationApplicationsWithPageableAndInvalidStatusValue() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        AdminRegistrationApplicationListPayload listPayload = new AdminRegistrationApplicationListPayload();
        listPayload.setPageable(AysPageable.generateFirstPage());

        if (listPayload.getFilter() == null) {
            listPayload.setFilter(new AdminRegistrationApplicationListPayload.Filter());
        }
        listPayload.getFilter().setStatuses(List.of(AdminRegistrationApplicationStatus.INVALID_STATUS));

        Response response = AdminRegistrationApplicationEndpoints.findAll(listPayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec());
    }

    @Test(groups = {"Smoke", "Regression"})
    public void listRegistrationApplicationsWithPageableAndOrders() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        AdminRegistrationApplicationListPayload listPayload = AdminRegistrationApplicationListPayload.generate();

        List<AysOrder> orders = AysOrder.generate("createdAt", "ASC");
        listPayload.getPageable().setOrders(orders);
        listPayload.setFilter(null);

        Response response = AdminRegistrationApplicationEndpoints.findAll(listPayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .body("response.content", notNullValue())
                .body("response.orderedBy", notNullValue())
                .body("response.filteredBy", nullValue());
    }

    @Test(groups = {"Regression"})
    public void listRegistrationApplicationsWithPageableAndInvalidOrdersData() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        AdminRegistrationApplicationListPayload listPayload = AdminRegistrationApplicationListPayload.generate();
        List<AysOrder> orders = AysOrder.generate("invalid", "ASC");
        listPayload.getPageable().setOrders(orders);

        Response response = AdminRegistrationApplicationEndpoints.findAll(listPayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec());
    }

    @Test(groups = {"Smoke", "Regression"})
    public void listRegistrationApplicationsWithPageableOrdersFilter() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        AdminRegistrationApplicationListPayload listPayload = AdminRegistrationApplicationListPayload.generate();

        List<AysOrder> orders = AysOrder.generate("createdAt", "ASC");
        listPayload.getPageable().setOrders(orders);
        listPayload.getFilter().setStatuses(List.of(AdminRegistrationApplicationStatus.WAITING));

        Response response = AdminRegistrationApplicationEndpoints.findAll(listPayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .body("response.content", notNullValue())
                .body("response.orderedBy", notNullValue())
                .body("response.filteredBy", notNullValue());
    }

    @Test(groups = {"Regression"})
    public void listRegistrationApplicationsWithPageableInvalidOrdersInvalidFilter() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        AdminRegistrationApplicationListPayload listPayload = AdminRegistrationApplicationListPayload.generate();

        listPayload.getFilter().setStatuses(List.of(AdminRegistrationApplicationStatus.WAITING));
        List<AysOrder> orders = AysOrder.generate("createdAt", "invalid");
        listPayload.getPageable().setOrders(orders);

        Response response = AdminRegistrationApplicationEndpoints.findAll(listPayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec());
    }

    private String loginAndGetAccessToken(LoginPayload loginPayload) {
        return AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");
    }

}