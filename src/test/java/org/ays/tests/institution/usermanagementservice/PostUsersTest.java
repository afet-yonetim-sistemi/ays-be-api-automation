package org.ays.tests.institution.usermanagementservice;

import io.restassured.response.Response;
import org.ays.auth.user.endpoints.UserEndpoints;
import org.ays.common.model.enums.ErrorMessage;
import org.ays.payload.AdminCredentials;
import org.ays.payload.Orders;
import org.ays.payload.Pageable;
import org.ays.payload.RequestBodyUsers;
import org.ays.payload.UsersFilter;
import org.ays.utility.AysLogUtil;
import org.ays.utility.AysResponseSpecs;
import org.ays.utility.DataProvider;
import org.ays.utility.DatabaseUtility;
import org.testng.annotations.Test;

import java.util.List;


public class PostUsersTest {

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void usersListForAdminOne() {
        AdminCredentials adminCredentials = AdminCredentials.generate();
        RequestBodyUsers requestBodyUsers = new RequestBodyUsers();
        requestBodyUsers.setPageable(Pageable.generate(1, 10));

        int totalElementCount = DatabaseUtility.verifyUserCountForFoundation("Volunteer Foundation");

        Response response = UserEndpoints.listUsers(requestBodyUsers, adminCredentials);

        if (response.jsonPath().getList("response.content").isEmpty()) {
            AysLogUtil.info("No users under this institution.");
            return;
        }

        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectTotalElementCount(totalElementCount));

    }

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void usersListForAdminTwo() {
        RequestBodyUsers requestBodyUsers = new RequestBodyUsers();
        requestBodyUsers.setPageable(Pageable.generate(1, 10));

        int totalElementCount = DatabaseUtility.verifyUserCountForFoundation("Disaster Foundation");

        Response response = UserEndpoints.listUsersTwo(requestBodyUsers);

        if (response.jsonPath().getList("response.content").isEmpty()) {
            AysLogUtil.info("No users under this institution.");
            return;
        }

        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectTotalElementCount(totalElementCount));

    }

    @Test(groups = {"Smoke", "Regression", "SuperAdmin", "Institution"})
    public void usersListForSuperAdmin() {
        RequestBodyUsers requestBodyUsers = new RequestBodyUsers();
        requestBodyUsers.setPageable(Pageable.generate(1, 10));

        int totalElementCount = DatabaseUtility.verifyUserCountForFoundation("Afet Yönetim Sistemi");

        Response response = UserEndpoints.listUsersSuperAdmin(requestBodyUsers);

        if (response.jsonPath().getList("response.content").isEmpty()) {
            AysLogUtil.info("No users under this institution.");
            return;
        }

        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectTotalElementCount(totalElementCount));

    }

    @Test(groups = {"Smoke", "Regression", "Institution", "SuperAdmin"})
    public void usersListWithAllFilter() {
        RequestBodyUsers requestBodyUsers = new RequestBodyUsers();
        requestBodyUsers.setPageable(Pageable.generate(1, 10));
        requestBodyUsers.setFilter(DatabaseUtility.fetchFirstUserData());
        Response response = UserEndpoints.listUsersSuperAdmin(requestBodyUsers);

        if (response.jsonPath().getList("response.content").isEmpty()) {
            AysLogUtil.info("No users under this institution.");
            return;
        }

        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectUserDetailsInContent());

    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidPropertyData", dataProviderClass = DataProvider.class)
    public void usersListForInvalidPropertyValue(String property, ErrorMessage errorMessage, String field, String type) {
        AdminCredentials adminCredentials = AdminCredentials.generate();
        RequestBodyUsers requestBodyUsers = new RequestBodyUsers();
        Pageable pageable = Pageable.generate(1, 10);
        List<Orders> ordersList = Orders.generate(property, "ASC");
        pageable.setOrders(ordersList);
        requestBodyUsers.setPageable(pageable);

        Response response = UserEndpoints.listUsers(requestBodyUsers, adminCredentials);

        List<Object> contentList = response.jsonPath().getList("response.content");

        if (contentList == null || contentList.isEmpty()) {
            AysLogUtil.info("No users under this institution.");
            return;
        }

        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));

    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidDirectionData", dataProviderClass = DataProvider.class)
    public void usersListForInvalidDirectionValue(String direction, ErrorMessage errorMessage, String field, String type) {
        AdminCredentials adminCredentials = AdminCredentials.generate();
        RequestBodyUsers requestBodyUsers = new RequestBodyUsers();
        Pageable pageable = Pageable.generate(1, 10);
        List<Orders> ordersList = Orders.generate("createdAt", direction);
        pageable.setOrders(ordersList);
        requestBodyUsers.setPageable(pageable);

        Response response = UserEndpoints.listUsers(requestBodyUsers, adminCredentials);

        List<Object> contentList = response.jsonPath().getList("response.content");

        if (contentList == null || contentList.isEmpty()) {
            AysLogUtil.info("No users under this institution.");
            return;
        }

        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));

    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidNames", dataProviderClass = DataProvider.class)
    public void usersListForInvalidFirstAndLasNameValue(String firstName, String lastName, ErrorMessage errorMessage, String field, String type) {
        AdminCredentials adminCredentials = AdminCredentials.generate();
        RequestBodyUsers requestBodyUsers = new RequestBodyUsers();
        requestBodyUsers.setPageable(Pageable.generate(1, 10));
        requestBodyUsers.setFilter(UsersFilter.generate(null, firstName, lastName, null, null));

        Response response = UserEndpoints.listUsers(requestBodyUsers, adminCredentials);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidCityDataForUsersList", dataProviderClass = DataProvider.class)
    public void usersListForInvalidCityData(String city, ErrorMessage errorMessage, String field, String type) {
        AdminCredentials adminCredentials = AdminCredentials.generate();
        RequestBodyUsers requestBodyUsers = new RequestBodyUsers();
        requestBodyUsers.setPageable(Pageable.generate(1, 10));
        requestBodyUsers.setFilter(UsersFilter.generate(null, null, null, city, null));

        Response response = UserEndpoints.listUsers(requestBodyUsers, adminCredentials);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidStatusesDataForUsersList", dataProviderClass = DataProvider.class)
    public void usersListForInvalidStatusesData(List<String> statuses, ErrorMessage errorMessage, String field, String type) {
        AdminCredentials adminCredentials = AdminCredentials.generate();
        RequestBodyUsers requestBodyUsers = new RequestBodyUsers();
        requestBodyUsers.setPageable(Pageable.generate(1, 10));
        requestBodyUsers.setFilter(UsersFilter.generate(null, null, null, null, statuses));

        Response response = UserEndpoints.listUsers(requestBodyUsers, adminCredentials);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

}

