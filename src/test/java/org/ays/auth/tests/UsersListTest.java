package org.ays.auth.tests;

import io.restassured.response.Response;
import org.ays.auth.datasource.UserDataSource;
import org.ays.auth.endpoints.AuthEndpoints;
import org.ays.auth.endpoints.UserEndpoints;
import org.ays.auth.model.entity.UserEntity;
import org.ays.auth.payload.LoginPayload;
import org.ays.auth.payload.UserListPayload;
import org.ays.common.model.enums.AysErrorMessage;
import org.ays.common.model.payload.AysOrder;
import org.ays.common.model.payload.AysPageable;
import org.ays.common.util.AysDataProvider;
import org.ays.common.util.AysLogUtil;
import org.ays.common.util.AysResponseSpecs;
import org.testng.annotations.Test;

import java.util.List;


public class UsersListTest {

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void usersListForAdminOne() {

        LoginPayload loginPayload = LoginPayload.generateAsAdminUserOne();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserListPayload userListPayload = new UserListPayload();
        userListPayload.setPageable(AysPageable.generate(1, 10));

        int totalElementCount = UserDataSource.findUserCountByInstitutionName("Volunteer Foundation");

        Response response = UserEndpoints.listUsers(userListPayload, accessToken);

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

        LoginPayload loginPayload = LoginPayload.generateAsAdminUserTwo();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserListPayload userListPayload = new UserListPayload();
        userListPayload.setPageable(AysPageable.generate(1, 10));

        int totalElementCount = UserDataSource.findUserCountByInstitutionName("Disaster Foundation");

        Response response = UserEndpoints.listUsers(userListPayload, accessToken);

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

        LoginPayload loginPayload = LoginPayload.generateAsSuperAdminUserOne();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserListPayload userListPayload = new UserListPayload();
        userListPayload.setPageable(AysPageable.generate(1, 10));

        int totalElementCount = UserDataSource.findUserCountByInstitutionName("Afet Yönetim Sistemi");

        Response response = UserEndpoints.listUsers(userListPayload, accessToken);

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

        LoginPayload loginPayload = LoginPayload.generateAsSuperAdminUserOne();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserListPayload userListPayload = new UserListPayload();
        userListPayload.setPageable(AysPageable.generate(1, 10));

        UserEntity userEntity = UserDataSource.findAnyUser();
        UserListPayload.Filter filter = UserListPayload.Filter.from(userEntity);
        userListPayload.setFilter(filter);
        Response response = UserEndpoints.listUsers(userListPayload, accessToken);

        if (response.jsonPath().getList("response.content").isEmpty()) {
            AysLogUtil.info("No users under this institution.");
            return;
        }

        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectUserDetailsInContent());

    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidPropertyData", dataProviderClass = AysDataProvider.class)
    public void usersListForInvalidPropertyValue(String property, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsAdminUserOne();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserListPayload userListPayload = new UserListPayload();
        AysPageable pageable = AysPageable.generate(1, 10);
        List<AysOrder> ordersList = AysOrder.generate(property, "ASC");
        pageable.setOrders(ordersList);
        userListPayload.setPageable(pageable);

        Response response = UserEndpoints.listUsers(userListPayload, accessToken);

        List<Object> contentList = response.jsonPath().getList("response.content");

        if (contentList == null || contentList.isEmpty()) {
            AysLogUtil.info("No users under this institution.");
            return;
        }

        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));

    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidDirectionData", dataProviderClass = AysDataProvider.class)
    public void usersListForInvalidDirectionValue(String direction, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsAdminUserOne();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserListPayload userListPayload = new UserListPayload();
        AysPageable pageable = AysPageable.generate(1, 10);
        List<AysOrder> ordersList = AysOrder.generate("createdAt", direction);
        pageable.setOrders(ordersList);
        userListPayload.setPageable(pageable);

        Response response = UserEndpoints.listUsers(userListPayload, accessToken);

        List<Object> contentList = response.jsonPath().getList("response.content");

        if (contentList == null || contentList.isEmpty()) {
            AysLogUtil.info("No users under this institution.");
            return;
        }

        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));

    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidNames", dataProviderClass = AysDataProvider.class)
    public void usersListForInvalidFirstAndLasNameValue(String firstName, String lastName, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsAdminUserOne();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserListPayload userListPayload = new UserListPayload();
        userListPayload.setPageable(AysPageable.generate(1, 10));

        UserListPayload.Filter filter = new UserListPayload.Filter();
        filter.setFirstName(firstName);
        filter.setLastName(lastName);
        userListPayload.setFilter(filter);

        Response response = UserEndpoints.listUsers(userListPayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidCityDataForUsersList", dataProviderClass = AysDataProvider.class)
    public void usersListForInvalidCityData(String city, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsAdminUserOne();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserListPayload userListPayload = new UserListPayload();
        userListPayload.setPageable(AysPageable.generate(1, 10));

        UserListPayload.Filter filter = new UserListPayload.Filter();
        filter.setCity(city);
        userListPayload.setFilter(filter);

        Response response = UserEndpoints.listUsers(userListPayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidStatusesDataForUsersList", dataProviderClass = AysDataProvider.class)
    public void usersListForInvalidStatusesData(List<String> statuses, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsAdminUserOne();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserListPayload userListPayload = new UserListPayload();
        userListPayload.setPageable(AysPageable.generate(1, 10));

        UserListPayload.Filter filter = new UserListPayload.Filter();
        filter.setStatuses(statuses);
        userListPayload.setFilter(filter);

        Response response = UserEndpoints.listUsers(userListPayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    private String loginAndGetAccessToken(LoginPayload loginPayload) {
        return AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");
    }

}

