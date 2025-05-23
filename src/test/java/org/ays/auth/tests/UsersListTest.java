package org.ays.auth.tests;

import io.restassured.response.Response;
import org.ays.auth.datasource.RoleDataSource;
import org.ays.auth.datasource.UserDataSource;
import org.ays.auth.endpoints.AuthEndpoints;
import org.ays.auth.endpoints.RoleEndpoints;
import org.ays.auth.endpoints.UserEndpoints;
import org.ays.auth.model.entity.UserEntity;
import org.ays.auth.model.enums.UserStatus;
import org.ays.auth.payload.LoginPayload;
import org.ays.auth.payload.RoleCreatePayload;
import org.ays.auth.payload.UserCreatePayload;
import org.ays.auth.payload.UserListPayload;
import org.ays.common.model.enums.AysErrorMessage;
import org.ays.common.model.payload.AysOrder;
import org.ays.common.model.payload.AysPageable;
import org.ays.common.util.AysConfigurationProperty;
import org.ays.common.util.AysDataProvider;
import org.ays.common.util.AysLogUtil;
import org.ays.common.util.AysResponseSpecs;
import org.testng.annotations.Test;
import org.testng.collections.CollectionUtils;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;


public class UsersListTest {

    @Test(groups = {"Smoke", "Regression"})
    public void listUsersForAdminOne() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserListPayload userListPayload = new UserListPayload();
        userListPayload.setPageable(AysPageable.generate(1, 10));

        int totalElementCount = UserDataSource.findUserCountByInstitutionId(AysConfigurationProperty.TestVolunteerFoundation.ID);

        Response response = UserEndpoints.findAll(userListPayload, accessToken);

        List<Object> content = response.jsonPath().getList("response.content");
        if (!CollectionUtils.hasElements(content)) {
            AysLogUtil.info("No users under this institution.");
            return;
        }

        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectTotalElementCount(totalElementCount));

    }

    @Test(groups = {"Smoke", "Regression"})
    public void listUsersForAdminTwo() {

        LoginPayload loginPayload = LoginPayload.generateAsTestDisasterFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserListPayload userListPayload = new UserListPayload();
        userListPayload.setPageable(AysPageable.generate(1, 10));

        int totalElementCount = UserDataSource.findUserCountByInstitutionId(AysConfigurationProperty.TestDisasterFoundation.ID);

        Response response = UserEndpoints.findAll(userListPayload, accessToken);

        List<Object> content = response.jsonPath().getList("response.content");
        if (!CollectionUtils.hasElements(content)) {
            AysLogUtil.info("No users under this institution.");
            return;
        }

        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectTotalElementCount(totalElementCount));

    }

    @Test(groups = {"Smoke", "Regression"})
    public void listUsersForSuperAdmin() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserListPayload userListPayload = new UserListPayload();
        userListPayload.setPageable(AysPageable.generate(1, 10));

        int totalElementCount = UserDataSource.findUserCountByInstitutionId(AysConfigurationProperty.TestVolunteerFoundation.ID);

        Response response = UserEndpoints.findAll(userListPayload, accessToken);

        List<Object> content = response.jsonPath().getList("response.content");
        if (!CollectionUtils.hasElements(content)) {
            AysLogUtil.info("No users under this institution.");
            return;
        }

        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectTotalElementCount(totalElementCount));

    }

    @Test(groups = {"Smoke", "Regression"})
    public void listUsersWithAllFilter() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationSuperAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserListPayload userListPayload = new UserListPayload();
        userListPayload.setPageable(AysPageable.generate(1, 10));

        UserEntity userEntity = UserDataSource.findAnyUser();
        UserListPayload.Filter filter = UserListPayload.Filter.from(userEntity);
        filter.setStatuses(List.of(UserStatus.ACTIVE.name(), UserStatus.PASSIVE.name()));
        userListPayload.setFilter(filter);
        Response response = UserEndpoints.findAll(userListPayload, accessToken);

        List<Object> content = response.jsonPath().getList("response.content");
        if (!CollectionUtils.hasElements(content)) {
            AysLogUtil.info("No users under this institution.");
            return;
        }

        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectUserDetailsInContent());

    }

    @Test(groups = {"Regression"}, dataProvider = "invalidPropertyData", dataProviderClass = AysDataProvider.class)
    public void listUsersForInvalidPropertyValue(String property, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserListPayload userListPayload = new UserListPayload();
        AysPageable pageable = AysPageable.generate(1, 10);
        List<AysOrder> ordersList = AysOrder.generate(property, "ASC");
        pageable.setOrders(ordersList);
        userListPayload.setPageable(pageable);

        Response response = UserEndpoints.findAll(userListPayload, accessToken);

        List<Object> content = response.jsonPath().getList("response.content");
        if (!CollectionUtils.hasElements(content)) {
            AysLogUtil.info("No users under this institution.");
            return;
        }

        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));

    }

    @Test(groups = {"Regression"}, dataProvider = "invalidDirectionData", dataProviderClass = AysDataProvider.class)
    public void listUsersForInvalidDirectionValue(String direction, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserListPayload userListPayload = new UserListPayload();
        AysPageable pageable = AysPageable.generate(1, 10);
        List<AysOrder> ordersList = AysOrder.generate("createdAt", direction);
        pageable.setOrders(ordersList);
        userListPayload.setPageable(pageable);

        Response response = UserEndpoints.findAll(userListPayload, accessToken);

        List<Object> content = response.jsonPath().getList("response.content");
        if (!CollectionUtils.hasElements(content)) {
            AysLogUtil.info("No users under this institution.");
            return;
        }

        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));

    }

    @Test(groups = {"Regression"}, dataProvider = "invalidNames", dataProviderClass = AysDataProvider.class)
    public void listUsersForInvalidFirstAndLastNameValue(String firstName, String lastName, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserListPayload userListPayload = new UserListPayload();
        userListPayload.setPageable(AysPageable.generate(1, 10));

        UserListPayload.Filter filter = new UserListPayload.Filter();
        filter.setFirstName(firstName);
        filter.setLastName(lastName);
        userListPayload.setFilter(filter);

        Response response = UserEndpoints.findAll(userListPayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression"})
    public void listUserByEmailAddress() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        RoleCreatePayload roleCreatePayload = RoleCreatePayload.generate();
        RoleEndpoints.create(roleCreatePayload, accessToken);
        String roleId = RoleDataSource.findLastCreatedRoleIdByInstitutionId(AysConfigurationProperty.TestVolunteerFoundation.ID);

        UserCreatePayload userCreatePayload = UserCreatePayload.generateUserWithARole(roleId);
        String emailAddress = userCreatePayload.getEmailAddress();
        UserEndpoints.create(userCreatePayload, accessToken);

        UserListPayload userListPayload = new UserListPayload();
        userListPayload.setPageable(AysPageable.generate(1, 10));

        UserListPayload.Filter filter = new UserListPayload.Filter();
        filter.setEmailAddress(emailAddress);

        userListPayload.setFilter(filter);

        Response response = UserEndpoints.findAll(userListPayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .body("response.content[0].emailAddress", equalTo(emailAddress))
                .body("response.content[0].id", notNullValue());

    }

    @Test(groups = {"Regression"})
    public void listAllUsersByEmailAddress() {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserListPayload userListPayload = new UserListPayload();
        userListPayload.setPageable(AysPageable.generate(1, 10));

        UserListPayload.Filter filter = new UserListPayload.Filter();
        filter.setEmailAddress("@afetyonetimsistemi.test");

        userListPayload.setFilter(filter);

        Response response = UserEndpoints.findAll(userListPayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .body("response.content.size()", greaterThan(0))
                .body("response.content[0].id", notNullValue())
                .body("response.content[0].firstName", notNullValue())
                .body("response.content[0].emailAddress", notNullValue());

    }

    @Test(groups = {"Regression"}, dataProvider = "invalidEmailAddressForUsersList", dataProviderClass = AysDataProvider.class)
    public void listUsersForInvalidEmailAddressData(String emailAddress, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserListPayload userListPayload = new UserListPayload();
        userListPayload.setPageable(AysPageable.generate(1, 10));

        UserListPayload.Filter filter = new UserListPayload.Filter();
        filter.setEmailAddress(emailAddress);
        userListPayload.setFilter(filter);

        Response response = UserEndpoints.findAll(userListPayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));

    }

    @Test(groups = {"Regression"}, dataProvider = "invalidCityDataForUsersList", dataProviderClass = AysDataProvider.class)
    public void listUsersForInvalidCityData(String city, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserListPayload userListPayload = new UserListPayload();
        userListPayload.setPageable(AysPageable.generate(1, 10));

        UserListPayload.Filter filter = new UserListPayload.Filter();
        filter.setCity(city);
        userListPayload.setFilter(filter);

        Response response = UserEndpoints.findAll(userListPayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    @Test(groups = {"Regression"}, dataProvider = "invalidStatusesDataForUsersList", dataProviderClass = AysDataProvider.class)
    public void listUsersForInvalidStatusesData(List<String> statuses, AysErrorMessage errorMessage, String field, String type) {

        LoginPayload loginPayload = LoginPayload.generateAsTestVolunteerFoundationAdmin();
        String accessToken = this.loginAndGetAccessToken(loginPayload);

        UserListPayload userListPayload = new UserListPayload();
        userListPayload.setPageable(AysPageable.generate(1, 10));

        UserListPayload.Filter filter = new UserListPayload.Filter();
        filter.setStatuses(statuses);
        userListPayload.setFilter(filter);

        Response response = UserEndpoints.findAll(userListPayload, accessToken);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.subErrorsSpec(errorMessage, field, type));
    }

    private String loginAndGetAccessToken(LoginPayload loginPayload) {
        return AuthEndpoints.token(loginPayload).jsonPath().getString("response.accessToken");
    }

}

