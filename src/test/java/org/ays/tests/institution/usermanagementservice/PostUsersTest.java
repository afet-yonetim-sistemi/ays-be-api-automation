package org.ays.tests.institution.usermanagementservice;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.payload.AdminCredentials;
import org.ays.payload.Orders;
import org.ays.payload.Pageable;
import org.ays.payload.RequestBodyUsers;
import org.ays.payload.SuperAdminCredentials;
import org.ays.utility.AysLogUtil;
import org.ays.utility.AysResponseSpecs;
import org.ays.utility.DataProvider;
import org.ays.utility.DatabaseUtility;
import org.ays.utility.ErrorMessage;
import org.testng.annotations.Test;

import java.util.List;


public class PostUsersTest {

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void usersListForAdminOne() {
        AdminCredentials adminCredentials = AdminCredentials.generate();
        RequestBodyUsers requestBodyUsers = new RequestBodyUsers();
        requestBodyUsers.setPageable(Pageable.generate(1, 10));
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers, adminCredentials);
        if (response.jsonPath().getList("response.content").isEmpty()) {
            AysLogUtil.info("No users under this institution.");
        } else {
            response.then()
                    .spec(AysResponseSpecs.expectSuccessResponseSpec())
                    .spec(AysResponseSpecs.expectTotalElementCountForVolunteer());
        }
    }

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void usersListForAdminTwo() {
        AdminCredentials adminCredentials = AdminCredentials.generateForAdminTwo();
        RequestBodyUsers requestBodyUsers = new RequestBodyUsers();
        requestBodyUsers.setPageable(Pageable.generate(1, 10));
        Response response = InstitutionEndpoints.listUsersTwo(requestBodyUsers, adminCredentials);
        if (response.jsonPath().getList("response.content").isEmpty()) {
            AysLogUtil.info("No users under this institution.");
        } else {
            response.then()
                    .spec(AysResponseSpecs.expectSuccessResponseSpec())
                    .spec(AysResponseSpecs.expectTotalElementCountForDisaster());
        }
    }

    @Test(groups = {"Smoke", "Regression", "SuperAdmin", "Institution"})
    public void usersListForSuperAdmin() {
        SuperAdminCredentials superAdminCredentials = SuperAdminCredentials.generate();
        RequestBodyUsers requestBodyUsers = new RequestBodyUsers();
        requestBodyUsers.setPageable(Pageable.generate(1, 10));
        Response response = InstitutionEndpoints.listUsersSuperAdmin(requestBodyUsers, superAdminCredentials);
        if (response.jsonPath().getList("response.content").isEmpty()) {
            AysLogUtil.info("No users under this institution.");
        } else {
            response.then()
                    .spec(AysResponseSpecs.expectSuccessResponseSpec())
                    .spec(AysResponseSpecs.expectTotalElementCountForAYS());
        }
    }

    @Test(groups = {"Smoke", "Regression", "Institution", "SuperAdmin"})
    public void usersListWithAllFilter() {
        SuperAdminCredentials superAdminCredentials = SuperAdminCredentials.generate();
        RequestBodyUsers requestBodyUsers = new RequestBodyUsers();
        requestBodyUsers.setPageable(Pageable.generate(1, 10));
        requestBodyUsers.setFilter(DatabaseUtility.fetchFirstUserData());
        Response response = InstitutionEndpoints.listUsersSuperAdmin(requestBodyUsers, superAdminCredentials);
        if (response.jsonPath().getList("response.content").isEmpty()) {
            AysLogUtil.info("No users under this institution.");
        } else {
            response.then()
                    .spec(AysResponseSpecs.expectSuccessResponseSpec())
                    .spec(AysResponseSpecs.expectUserDetailsInContent());
        }
    }

    @Test(groups = {"Smoke", "Regression", "Institution"}, dataProvider = "invalidPropertyData", dataProviderClass = DataProvider.class)
    public void usersListForInvalidPropertyValue(String property, ErrorMessage errorMessage, String field, String type) {
        AdminCredentials adminCredentials = AdminCredentials.generate();
        RequestBodyUsers requestBodyUsers = new RequestBodyUsers();
        Pageable pageable = Pageable.generate(1, 10);
        List<Orders> ordersList = Orders.generate(property, "ASC");
        pageable.setOrders(ordersList);
        requestBodyUsers.setPageable(pageable);

        Response response = InstitutionEndpoints.listUsers(requestBodyUsers, adminCredentials);

        List<Object> contentList = response.jsonPath().getList("response.content");

        if (contentList == null || contentList.isEmpty()) {
            AysLogUtil.info("No users under this institution.");
        } else {
            response.then()
                    .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                    .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
        }
    }

    @Test(groups = {"Smoke", "Regression", "Institution"}, dataProvider = "invalidDirectionData", dataProviderClass = DataProvider.class)
    public void usersListForInvalidDirectionValue(String direction, ErrorMessage errorMessage, String field, String type) {
        AdminCredentials adminCredentials = AdminCredentials.generate();
        RequestBodyUsers requestBodyUsers = new RequestBodyUsers();
        Pageable pageable = Pageable.generate(1, 10);
        List<Orders> ordersList = Orders.generate("createdAt", direction);
        pageable.setOrders(ordersList);
        requestBodyUsers.setPageable(pageable);

        Response response = InstitutionEndpoints.listUsers(requestBodyUsers, adminCredentials);

        List<Object> contentList = response.jsonPath().getList("response.content");

        if (contentList == null || contentList.isEmpty()) {
            AysLogUtil.info("No users under this institution.");
        } else {
            response.then()
                    .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                    .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
        }
    }

}
