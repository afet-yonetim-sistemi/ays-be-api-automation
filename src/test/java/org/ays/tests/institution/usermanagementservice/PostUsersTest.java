package org.ays.tests.institution.usermanagementservice;

import io.restassured.response.Response;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.payload.*;
import org.ays.utility.AysLogUtil;
import org.ays.utility.AysResponseSpecs;
import org.ays.utility.DataProvider;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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
        requestBodyUsers.setPagination(Pagination.createPagination());
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
        requestBodyUsers.setPagination(Pagination.setPagination(page, pageSize));
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
        response.then()
                .spec(AysResponseSpecs.expectSuccessResponseSpec())
                .spec(AysResponseSpecs.expectUnfilteredListResponseSpec());

    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "negativePaginationData", dataProviderClass = DataProvider.class)
    public void listUsersWithNegativePaginationScenarios(int page, int pageSize) {
        requestBodyUsers.setPagination(Pagination.setPagination(page, pageSize));
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must be between 1 and 99999999"))
                .body("subErrors[0].field", anyOf(equalTo("page"), equalTo("pageSize")))
                .body("subErrors[0].type", equalTo("int"));

    }

    @Test(groups = {"Regression", "Institution"})
    public void listUsersWithNullPagination() {
        Pagination pagination = new Pagination();
        requestBodyUsers.setPagination(pagination);
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .spec(AysResponseSpecs.expectInvalidPaginationErrors());

    }

    @Test(groups = {"Regression", "Institution"}, dataProvider = "invalidNames", dataProviderClass = DataProvider.class)
    public void listUsersWithNegativeNameScenariosFilter(String firstname, String lastname, String errorMessage) {
        requestBodyUsers.setFilter(FiltersForUsers.generateCreateFilterWithUserFirstAndLastName(firstname, lastname));
        requestBodyUsers.setPagination(Pagination.createPagination());
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", anyOf(equalTo("lastName"), equalTo("firstName")))
                .body("subErrors[0].type", equalTo("String"));

    }

    @Test(groups = {"Smoke", "Regression", "Institution"}, description = "Prior to executing this method, a user is created to prevent failures in case no user is associated with the institution.")
    public void listUsersWithValidUserNameFilter() {
        User user = User.generateUserPayload();
        InstitutionEndpoints.createAUser(user);
        requestBodyUsers.setFilter(FiltersForUsers.generateCreateFilterWithUserFirstAndLastName(user.getFirstName(), user.getLastName()));
        requestBodyUsers.setPagination(Pagination.createPagination());
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
        UserCredentials.generateCreate();
        requestBodyUsers.setFilter(FiltersForUsers.generateCreateFilterWithUserStatus("ACTIVE"));
        requestBodyUsers.setPagination(Pagination.createPagination());
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
        UserCredentials.generateCreate();
        requestBodyUsers.setFilter(FiltersForUsers.generateCreateFilterWithUserStatus("ACT"));
        requestBodyUsers.setPagination(Pagination.createPagination());
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec());
    }

    @Test(groups = {"Smoke", "Regression", "Institution"}, description = "Prior to executing this method, a user is created to prevent failures in case no user is associated with the institution.")
    public void listUsersWithValidSupportStatusFilter() {
        UserCredentials.generateCreate();
        requestBodyUsers.setFilter(FiltersForUsers.generateCreateFilterWithUserSupportStatus("IDLE"));
        requestBodyUsers.setPagination(Pagination.createPagination());
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
        requestBodyUsers.setFilter(FiltersForUsers.generateCreateFilterWithUserSupportStatus("Ready"));
        requestBodyUsers.setPagination(Pagination.createPagination());
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec());
    }

    @Test(groups = {"Smoke", "Regression", "Institution"}, description = "Prior to executing this method, a user is created to prevent failures in case no user is associated with the institution.")
    public void listUsersWithValidPhoneNumberFilter() {
        User user = User.generateUserPayload();
        InstitutionEndpoints.createAUser(user);
        requestBodyUsers.setFilter(FiltersForUsers.generateCreateFilterWithUserPhoneNumber(user.getPhoneNumber()));
        requestBodyUsers.setPagination(Pagination.createPagination());
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
        UserCredentials.generateCreate();
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setCountryCode(countryCode);
        phoneNumber.setLineNumber(lineNumber);
        requestBodyUsers.setFilter(FiltersForUsers.generateCreateFilterWithUserPhoneNumber(phoneNumber));
        requestBodyUsers.setPagination(Pagination.createPagination());
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", either(equalTo("lineNumber")).or(equalTo("countryCode")))
                .body("subErrors[0].type", equalTo("String"));

    }

    @Test(groups = {"Smoke", "Regression", "Institution"}, description = "Prior to executing this method, a user is created to prevent failures in case no user is associated with the institution.")
    public void listUsersWithValidFiltersAndSort() {
        User user = User.generateUserPayload();
        InstitutionEndpoints.createAUser(user);
        requestBodyUsers.setFilter(FiltersForUsers.generateCreateFilterWithAllUserFilters(user.getPhoneNumber(), user.getFirstName(), user.getLastName(), "ACTIVE", "IDLE"));
        requestBodyUsers.setSort(Sort.generateCreateSortBody("createdAt", "ASC"));
        requestBodyUsers.setPagination(Pagination.createPagination());
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
        requestBodyUsers.setPagination(Pagination.createPagination());
        requestBodyUsers.setSort(Sort.generateCreateSortBody("createdAt", "ASC"));
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
        User user1 = User.generateUserPayload();
        InstitutionEndpoints.createAUser(user1);
        User user2 = User.generateUserPayload();
        InstitutionEndpoints.createAUser(user2);

        requestBodyUsers.setPagination(Pagination.createPagination());
        requestBodyUsers.setSort(Sort.generateCreateSortBody("createdAt", "DESC"));
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
        requestBodyUsers.setPagination(Pagination.createPagination());
        requestBodyUsers.setSort(Sort.generateCreateSortBody(property, direction));
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
        response.then()
                .spec(AysResponseSpecs.expectBadRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage));

    }

}
