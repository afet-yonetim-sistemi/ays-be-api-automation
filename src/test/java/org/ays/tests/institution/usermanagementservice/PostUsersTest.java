package org.ays.tests.institution.usermanagementservice;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.payload.Orders;
import org.ays.payload.Pageable;
import org.ays.payload.PhoneNumber;
import org.ays.payload.RequestBodyUsers;
import org.ays.payload.User;
import org.ays.payload.UsersFilter;
import org.ays.utility.AysLogUtil;
import org.ays.utility.AysResponseSpecs;
import org.ays.utility.DataProvider;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItem;

public class PostUsersTest {
    RequestBodyUsers requestBodyUsers;

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        requestBodyUsers = new RequestBodyUsers();
    }

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void validateUserDetailsInUsersList() {
        requestBodyUsers.setPageable(Pageable.generateFirstPage());
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
        if (response.jsonPath().getList("response.content").isEmpty()) {
            AysLogUtil.info("No users under this institution.");
        } else {
            response.then()
                    .spec(AysResponseSpecs.expectSuccessResponseSpec())
                    .spec(AysResponseSpecs.expectUserDetailsInContent())
                    .spec(AysResponseSpecs.expectUnfilteredListResponseSpec());
        }
    }

    @Test(groups = {"Smoke", "Regression", "Institution"}, dataProvider = "positivePaginationData", dataProviderClass = DataProvider.class)
    public void listUsersWithPositivePaginationScenarios(int page, int pageSize) {
        requestBodyUsers.setPageable(Pageable.generate(page, pageSize));
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectUnfilteredListResponseSpec());

    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "negativePageableData", dataProviderClass = DataProvider.class)
    public void listUsersWithNegativePaginationScenarios(int page, int pageSize) {
        requestBodyUsers.setPageable(Pageable.generate(page, pageSize));
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must be between 1 and 99999999"))
                .body("subErrors[0].field", anyOf(equalTo("page"), equalTo("pageSize")))
                .body("subErrors[0].type", equalTo("int"));

    }

    @Test(groups = {"Regression", "Institution"})
    public void listUsersWithNullPagination() {
        Pageable pageable = new Pageable();
        requestBodyUsers.setPageable(pageable);
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.expectInvalidPageableErrors());

    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidNames", dataProviderClass = DataProvider.class)
    public void listUsersWithNegativeNameScenariosFilter(String firstname, String lastname, String errorMessage) {
        requestBodyUsers.setFilter(UsersFilter.generate(null, firstname, lastname, null, null));
        requestBodyUsers.setPageable(Pageable.generateFirstPage());
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", anyOf(equalTo("lastName"), equalTo("firstName")))
                .body("subErrors[0].type", equalTo("String"));

    }

    @Test(groups = {"Smoke", "Regression", "Institution"}, description = "Prior to executing this method, a user is created to prevent failures in case no user is associated with the institution.")
    public void listUsersWithValidUserNameFilter() {
        User user = User.generate();
        InstitutionEndpoints.createAUser(user);
        requestBodyUsers.setFilter(UsersFilter.generate(null, user.getFirstName(), user.getLastName(), null, null));
        requestBodyUsers.setPageable(Pageable.generateFirstPage());
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectDefaultListingDetails())
                .spec(AysResponseSpecs.expectUserDetailsInContent())
                .body("response.sortedBy", equalTo(null))
                .body("response.filteredBy.firstName", equalTo(user.getFirstName()))
                .body("response.filteredBy.lastName", equalTo(user.getLastName()));
    }

    @Test(groups = {"Smoke", "Regression", "Institution"}, description = "Prior to executing this method, a user is created to prevent failures in case no user is associated with the institution.")
    public void listUsersWithValidStatusFilter() {
        InstitutionEndpoints.generateANewUser();
        List<String> statuses = new ArrayList<>();
        statuses.add("ACTIVE");
        requestBodyUsers.setFilter(UsersFilter.generate(null, null, null, statuses, null));
        requestBodyUsers.setPageable(Pageable.generateFirstPage());
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectDefaultListingDetails())
                .spec(AysResponseSpecs.expectUserDetailsInContent())
                .body("response.sortedBy", equalTo(null))
                .body("response.filteredBy.statuses", hasItem("ACTIVE"))
                .body("response.content*.status", everyItem(equalTo("ACTIVE")));
    }

    @Test(groups = {"Regression", "Institution"}, description = "Prior to executing this method, a user is created to prevent failures in case no user is associated with the institution.")
    public void listUsersWithInvalidStatusFilter() {
        InstitutionEndpoints.generateANewUser();
        List<String> statuses = new ArrayList<>();
        statuses.add("ACT");
        requestBodyUsers.setFilter(UsersFilter.generate(null, null, null, statuses, null));
        requestBodyUsers.setPageable(Pageable.generateFirstPage());
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec());
    }

    @Test(groups = {"Smoke", "Regression", "Institution"}, description = "Prior to executing this method, a user is created to prevent failures in case no user is associated with the institution.")
    public void listUsersWithValidSupportStatusFilter() {
        InstitutionEndpoints.generateANewUser();
        List<String> supportStatuses = new ArrayList<>();
        supportStatuses.add("IDLE");
        requestBodyUsers.setFilter(UsersFilter.generate(null, null, null, null, supportStatuses));
        requestBodyUsers.setPageable(Pageable.generateFirstPage());
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectDefaultListingDetails())
                .spec(AysResponseSpecs.expectUserDetailsInContent())
                .body("response.sortedBy", equalTo(null))
                .body("response.filteredBy.supportStatuses", hasItem("IDLE"))
                .body("response.content*.supportStatus", everyItem(equalTo("IDLE")));
    }

    @Test(groups = {"Regression", "Institution"})
    public void listUsersWithInvalidSupportStatusFilter() {
        List<String> supportStatuses = new ArrayList<>();
        supportStatuses.add("Ready");
        requestBodyUsers.setFilter(UsersFilter.generate(null, null, null, null, supportStatuses));
        requestBodyUsers.setPageable(Pageable.generateFirstPage());
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec());
    }

    @Test(groups = {"Smoke", "Regression", "Institution"}, description = "Prior to executing this method, a user is created to prevent failures in case no user is associated with the institution.")
    public void listUsersWithValidPhoneNumberFilter() {
        User user = User.generate();
        InstitutionEndpoints.createAUser(user);
        requestBodyUsers.setFilter(UsersFilter.generate(user.getPhoneNumber(), null, null, null, null));
        requestBodyUsers.setPageable(Pageable.generateFirstPage());
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectDefaultListingDetails())
                .spec(AysResponseSpecs.expectUserDetailsInContent())
                .body("response.sortedBy", equalTo(null))
                .body("response.filteredBy.phoneNumber.countryCode", equalTo(user.getPhoneNumber().getCountryCode()))
                .body("response.filteredBy.phoneNumber.lineNumber", equalTo(user.getPhoneNumber().getLineNumber()));

    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidPhoneNumberDataForFilter", dataProviderClass = DataProvider.class, description = "Prior to executing this method, a user is created to prevent failures in case no user is associated with the institution.")
    public void listUsersWithInvalidPhoneNumberFilter(String countryCode, String lineNumber, String errorMessage) {
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setCountryCode(countryCode);
        phoneNumber.setLineNumber(lineNumber);
        requestBodyUsers.setFilter(UsersFilter.generate(phoneNumber, null, null, null, null));
        requestBodyUsers.setPageable(Pageable.generateFirstPage());
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", either(equalTo("lineNumber")).or(equalTo("countryCode")))
                .body("subErrors[0].type", equalTo("String"));

    }

    @Test(groups = {"Smoke", "Regression", "Institution"}, description = "Prior to executing this method, a user is created to prevent failures in case no user is associated with the institution.")
    public void listUsersWithValidFiltersAndSort() {
        User user = User.generate();
        List<String> supportStatuses = new ArrayList<>();
        supportStatuses.add("IDLE");
        List<String> statuses = new ArrayList<>();
        statuses.add("ACTIVE");
        InstitutionEndpoints.createAUser(user);
        requestBodyUsers.setFilter(UsersFilter.generate(user.getPhoneNumber(), user.getFirstName(), user.getLastName(), statuses, supportStatuses));
        requestBodyUsers.setOrders(Orders.generate("createdAt", "ASC"));
        requestBodyUsers.setPageable(Pageable.generateFirstPage());
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectDefaultListingDetails())
                .spec(AysResponseSpecs.expectUserDetailsInContent())
                .body("response.sortedBy[0].property", equalTo("createdAt"))
                .body("response.sortedBy[0].direction", equalTo("ASC"))
                .body("response.filteredBy.firstName", equalTo(user.getFirstName()))
                .body("response.filteredBy.lastName", equalTo(user.getLastName()))
                .body("response.filteredBy.supportStatuses", hasItem("IDLE"))
                .body("response.filteredBy.statuses", hasItem("ACTIVE"))
                .body("response.filteredBy.phoneNumber.countryCode", equalTo(user.getPhoneNumber().getCountryCode()))
                .body("response.filteredBy.phoneNumber.lineNumber", equalTo(user.getPhoneNumber().getLineNumber()));
    }

    @Test(groups = {"Smoke", "Regression", "Institution"})
    public void listUsersWithValidSortOptionsInAscendingOrder() {
        requestBodyUsers.setPageable(Pageable.generateFirstPage());
        requestBodyUsers.setOrders(Orders.generate("createdAt", "ASC"));
        Response ascResponse = InstitutionEndpoints.listUsers(requestBodyUsers);
        ascResponse.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectDefaultListingDetails())
                .spec(AysResponseSpecs.expectUserDetailsInContent())
                .body("response.filteredBy", equalTo(null))
                .body("response.sortedBy[0].property", equalTo("createdAt"))
                .body("response.sortedBy[0].direction", equalTo("ASC"));

    }

    @Test(groups = {"Smoke", "Regression", "Institution"}, description = "Prior to executing this method, two users are created to prevent failures in case no user is associated with the institution and validate DESC order.")
    public void listUsersWithValidSortOptionsInDescendingOrder() {
        User user1 = User.generate();
        InstitutionEndpoints.createAUser(user1);
        User user2 = User.generate();
        InstitutionEndpoints.createAUser(user2);

        requestBodyUsers.setPageable(Pageable.generateFirstPage());
        requestBodyUsers.setOrders(Orders.generate("createdAt", "DESC"));
        Response ascResponse = InstitutionEndpoints.listUsers(requestBodyUsers);
        ascResponse.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectDefaultListingDetails())
                .body("response.content[0].firstName", equalTo(user2.getFirstName()))
                .body("response.content[0].lastName", equalTo(user2.getLastName()))
                .body("response.content[1].firstName", equalTo(user1.getFirstName()))
                .body("response.content[1].lastName", equalTo(user1.getLastName()))
                .body("response.filteredBy", equalTo(null))
                .body("response.sortedBy[0].property", equalTo("createdAt"))
                .body("response.sortedBy[0].direction", equalTo("DESC"));

    }

    @Test(groups = {"Smoke", "Regression", "Institution"}, dataProvider = "negativeSortData", dataProviderClass = DataProvider.class)
    public void sortUsersWithInvalidSortOptions(String property, String direction, String errorMessage) {
        requestBodyUsers.setPageable(Pageable.generateFirstPage());
        requestBodyUsers.setOrders(Orders.generate(property, direction));
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage));

    }

}
