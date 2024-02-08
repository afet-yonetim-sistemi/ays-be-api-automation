package tests.institution.usermanagementservice;

import endpoints.InstitutionEndpoints;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import payload.*;
import utility.DataProvider;

import java.util.List;

import static org.hamcrest.Matchers.*;

public class PostUsersTest extends DataProvider {
    RequestBodyUsers requestBodyUsers;

    @BeforeMethod
    public void setup() {
        requestBodyUsers = new RequestBodyUsers();
    }

    @Test(dataProvider = "positivePaginationData")
    @Story("As an Institution admin I want to list all users")
    @Severity(SeverityLevel.NORMAL)
    public void listUsersWithPositivePaginationScenarios(int page, int pageSize) {
        requestBodyUsers.setPagination(Helper.setPagination(page, pageSize));
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
        response.then()
                .spec(successResponseSpec())
                .body("response.content", instanceOf(List.class))
                .body("response.pageNumber", equalTo(page))
                .body("response.totalPageCount", notNullValue())
                .body("response.totalElementCount", notNullValue())
                .body("response", hasKey("sortedBy"))
                .body("response", hasKey("filteredBy"));

    }

    @Test(dataProvider = "negativePaginationData")
    @Story("As an Institution admin I want to get a proper error message when pagination values are invalid")
    @Severity(SeverityLevel.NORMAL)
    public void listUsersWithNegativePaginationScenarios(int page, int pageSize) {
        requestBodyUsers.setPagination(Helper.setPagination(page, pageSize));
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must be between 1 and 99999999"))
                .body("subErrors[0].field", anyOf(equalTo("page"), equalTo("pageSize")))
                .body("subErrors[0].type", equalTo("int"));

    }

    @Test()
    @Story("As an Institution admin I want to get a proper error message when pagination values are null")
    @Severity(SeverityLevel.NORMAL)
    public void listUsersWithNullPagination() {
        Pagination pagination = new Pagination();
        requestBodyUsers.setPagination(pagination);
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must be between 1 and 99999999"))
                .body("subErrors[0].field", anyOf(equalTo("page"), equalTo("pageSize")))
                .body("subErrors[0].type", equalTo("int"))
                .body("subErrors[1].message", equalTo("must be between 1 and 99999999"))
                .body("subErrors[1].field", anyOf(equalTo("page"), equalTo("pageSize")))
                .body("subErrors[1].type", equalTo("int"));

    }

    @Test(dataProvider = "invalidNames")
    @Story("As an Institution admin I want to get a proper error message when firstname or/and lastname are invalid.")
    @Severity(SeverityLevel.NORMAL)
    public void listUsersWithNegativeNameScenariosFilter(String firstname, String lastname, String errorMessage) {
        requestBodyUsers.setFilter(Helper.createFilterWithUserFirstAndLastName(firstname, lastname));
        requestBodyUsers.setPagination(Helper.createPagination());
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage))
                .body("subErrors[0].field", anyOf(equalTo("lastName"), equalTo("firstName")))
                .body("subErrors[0].type", equalTo("String"));

    }

    @Test()
    @Story("As an Institution admin I want to filter users by firstName and LastName")
    @Severity(SeverityLevel.NORMAL)
    @Description("Prior to executing this method, a user is created to prevent failures in case no user is associated with the institution.")
    public void listUsersWithValidUserNameFilter() {
        User user = Helper.createUserPayload();
        InstitutionEndpoints.createAUser(user);
        requestBodyUsers.setFilter(Helper.createFilterWithUserFirstAndLastName(user.getFirstName(), user.getLastName()));
        requestBodyUsers.setPagination(Helper.createPagination());
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
        response.then()
                .spec(successResponseSpec())
                .body("response.content", hasSize(greaterThan(0)))
                .body("response.content[0].id", notNullValue())
                .body("response.content[0].firstName", equalTo(user.getFirstName()))
                .body("response.content[0].lastName", equalTo(user.getLastName()))
                .body("response.content[0].status", equalTo("ACTIVE"))
                .body("response.content[0].role", equalTo("VOLUNTEER"))
                .body("response.content[0].supportStatus", equalTo("IDLE"))
                .body("response.pageNumber", equalTo(1))
                .body("response.totalPageCount", notNullValue())
                .body("response.totalElementCount", notNullValue())
                .body("response", hasKey("sortedBy"))
                .body("response", hasKey("filteredBy"));
    }

    @Test()
    @Story("As an Institution admin I want to filter users by their status")
    @Severity(SeverityLevel.NORMAL)
    @Description("Prior to executing this method, a user is created to prevent failures in case no user is associated with the institution.")
    public void listUsersWithValidStatusFilter() {
        User user = Helper.createUserPayload();
        InstitutionEndpoints.createAUser(user);
        requestBodyUsers.setFilter(Helper.createFilterWithUserStatus("ACTIVE"));
        requestBodyUsers.setPagination(Helper.createPagination());
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
        response.then()
                .spec(successResponseSpec())
                .body("response.content", hasSize(greaterThan(0)))
                .body("response.content[0].id", notNullValue())
                .body("response.content[0].firstName", notNullValue())
                .body("response.content[0].lastName", notNullValue())
                .body("response.content[0].status", equalTo("ACTIVE"))
                .body("response.content[0].role", equalTo("VOLUNTEER"))
                .body("response.pageNumber", equalTo(1))
                .body("response.totalPageCount", notNullValue())
                .body("response.totalElementCount", notNullValue())
                .body("response", hasKey("sortedBy"))
                .body("response", hasKey("filteredBy"));
    }

    @Test()
    @Story("As an Institution admin I want to get a proper error message when I filter users with invalid user status")
    @Severity(SeverityLevel.NORMAL)
    @Description("Prior to executing this method, a user is created to prevent failures in case no user is associated with the institution.")
    public void listUsersWithInvalidStatusFilter() {
        User user = Helper.createUserPayload();
        InstitutionEndpoints.createAUser(user);
        requestBodyUsers.setFilter(Helper.createFilterWithUserStatus("ACT"));
        requestBodyUsers.setPagination(Helper.createPagination());
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
        response.then()
                .spec(badRequestResponseSpec());
    }

    @Test()
    @Story("As an Institution admin I want to filter users by their support status")
    @Severity(SeverityLevel.NORMAL)
    @Description("Prior to executing this method, a user is created to prevent failures in case no user is associated with the institution.")
    public void listUsersWithValidSupportStatusFilter() {
        Helper.createNewUser();
        requestBodyUsers.setFilter(Helper.createFilterWithUserSupportStatus("IDLE"));
        requestBodyUsers.setPagination(Helper.createPagination());
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
        response.then()
                .spec(successResponseSpec())
                .body("response.content", hasSize(greaterThan(0)))
                .body("response.content[0].supportStatus", equalTo("IDLE"))
                .body("response.pageNumber", equalTo(1))
                .body("response.totalPageCount", notNullValue())
                .body("response.totalElementCount", notNullValue())
                .body("response", hasKey("sortedBy"))
                .body("response", hasKey("filteredBy"));
    }

    @Test()
    @Story("As an Institution admin I want to get a proper error message when I filter users with invalid user support status")
    @Severity(SeverityLevel.NORMAL)
    public void listUsersWithInvalidSupportStatusFilter() {
        requestBodyUsers.setFilter(Helper.createFilterWithUserSupportStatus("Ready"));
        requestBodyUsers.setPagination(Helper.createPagination());
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
        response.then()
                .spec(badRequestResponseSpec());
    }

    @Test()
    @Story("As an Institution admin I want to filter users by their phone number")
    @Severity(SeverityLevel.NORMAL)
    @Description("Prior to executing this method, a user is created to prevent failures in case no user is associated with the institution.")
    public void listUsersWithValidPhoneNumberFilter() {
        User user = Helper.createUserPayload();
        InstitutionEndpoints.createAUser(user);
        requestBodyUsers.setFilter(Helper.createFilterWithUserPhoneNumber(user.getPhoneNumber()));
        requestBodyUsers.setPagination(Helper.createPagination());
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
        response.then()
                .spec(successResponseSpec())
                .body("response.content", hasSize(greaterThan(0)))
                .body("response.content[0].id", notNullValue())
                .body("response.content[0].firstName", equalTo(user.getFirstName()))
                .body("response.content[0].lastName", equalTo(user.getLastName()))
                .body("response.content[0].status", equalTo("ACTIVE"))
                .body("response.content[0].role", equalTo("VOLUNTEER"))
                .body("response.content[0].supportStatus", equalTo("IDLE"))
                .body("response.pageNumber", equalTo(1))
                .body("response.totalPageCount", notNullValue())
                .body("response.totalElementCount", notNullValue())
                .body("response", hasKey("sortedBy"))
                .body("response", hasKey("filteredBy"));
    }

    @Test(dataProvider = "invalidPhoneNumberData")
    @Story("As an Institution admin I want to get a proper error message when I filter users with invalid phone number")
    @Severity(SeverityLevel.NORMAL)
    public void listUsersWithInvalidPhoneNumberFilter(String countryCode, String lineNumber) {
        Helper.createNewUser();
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setCountryCode(countryCode);
        phoneNumber.setLineNumber(lineNumber);
        requestBodyUsers.setFilter(Helper.createFilterWithUserPhoneNumber(phoneNumber));
        requestBodyUsers.setPagination(Helper.createPagination());
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors[0].message", equalTo("MUST BE VALID"))
                .body("subErrors[0].field", equalTo("phoneNumber"))
                .body("subErrors[0].type", equalTo("AysPhoneNumberFilterRequest"));

    }

    @Test()
    @Story("As an Institution admin I want to filter users by all filters and sort")
    @Severity(SeverityLevel.NORMAL)
    @Description("Prior to executing this method, a user is created to prevent failures in case no user is associated with the institution.")
    public void listUsersWithValidFiltersAndSort() {
        User user = Helper.createUserPayload();
        InstitutionEndpoints.createAUser(user);
        requestBodyUsers.setFilter(Helper.createFilterWithAllUserFilters(user.getPhoneNumber(), user.getFirstName(), user.getLastName(), "ACTIVE", "IDLE"));
        requestBodyUsers.setSort(Helper.createSortBody("createdAt", "ASC"));
        requestBodyUsers.setPagination(Helper.createPagination());
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
        response.then()
                .spec(successResponseSpec())
                .body("response.content", hasSize(greaterThan(0)))
                .body("response.content[0].id", notNullValue())
                .body("response.content[0].firstName", equalTo(user.getFirstName()))
                .body("response.content[0].lastName", equalTo(user.getLastName()))
                .body("response.content[0].status", equalTo("ACTIVE"))
                .body("response.content[0].role", equalTo("VOLUNTEER"))
                .body("response.content[0].supportStatus", equalTo("IDLE"))
                .body("response.pageNumber", equalTo(1))
                .body("response.totalPageCount", notNullValue())
                .body("response.totalElementCount", notNullValue())
                .body("response.sortedBy[0].property", equalTo("createdAt"))
                .body("response.sortedBy[0].direction", equalTo("ASC"))
                .body("response.filteredBy", nullValue());
    }

    @Test()
    @Story("As an Institution admin I want to sort users with valid sort options in ascending order")
    @Severity(SeverityLevel.NORMAL)
    public void listUsersWithValidSortOptionsInAscendingOrder() {
        requestBodyUsers.setPagination(Helper.createPagination());
        requestBodyUsers.setSort(Helper.createSortBody("createdAt", "ASC"));
        Response ascResponse = InstitutionEndpoints.listUsers(requestBodyUsers);
        ascResponse.then()
                .spec(successResponseSpec())
                .body("response.sortedBy[0].property", equalTo("createdAt"))
                .body("response.sortedBy[0].direction", equalTo("ASC"));

    }

    @Test()
    @Story("As an Institution admin I want to sort users with valid sort options in descending order")
    @Severity(SeverityLevel.NORMAL)
    @Description("Prior to executing this method, two users are created to prevent failures in case no user is associated with the institution and validate DESC order.")
    public void sortUsersWithValidSortOptionsInDescendingOrder() {
        User user1 = Helper.createUserPayload();
        InstitutionEndpoints.createAUser(user1);
        User user2 = Helper.createUserPayload();
        InstitutionEndpoints.createAUser(user2);

        requestBodyUsers.setPagination(Helper.createPagination());
        requestBodyUsers.setSort(Helper.createSortBody("createdAt", "DESC"));
        Response ascResponse = InstitutionEndpoints.listUsers(requestBodyUsers);
        ascResponse.then()
                .log().body()
                .spec(successResponseSpec())
                .body("response.content[0].firstName", equalTo(user2.getFirstName()))
                .body("response.content[0].lastName", equalTo(user2.getLastName()))
                .body("response.content[1].firstName", equalTo(user1.getFirstName()))
                .body("response.content[1].lastName", equalTo(user1.getLastName()))
                .body("response.sortedBy[0].property", equalTo("createdAt"))
                .body("response.sortedBy[0].direction", equalTo("DESC"));

    }

    @Test(dataProvider = "negativeSortData")
    @Story("As an Institution admin when I sort users with invalid sort options I want to get proper error message")
    @Severity(SeverityLevel.NORMAL)
    public void sortUsersWithInvalidSortOptions(String property, String direction, String errorMessage) {
        requestBodyUsers.setPagination(Helper.createPagination());
        requestBodyUsers.setSort(Helper.createSortBody(property, direction));
        Response response = InstitutionEndpoints.listUsers(requestBodyUsers);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors[0].message", equalTo(errorMessage));

    }

    private ResponseSpecification successResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType("application/json")
                .expectBody("time", notNullValue())
                .expectBody("httpStatus", equalTo("OK"))
                .expectBody("isSuccess", equalTo(true))
                .build();
    }

    private ResponseSpecification badRequestResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(400)
                .expectContentType("application/json")
                .expectBody("time", notNullValue())
                .expectBody("httpStatus", equalTo("BAD_REQUEST"))
                .expectBody("header", equalTo("VALIDATION ERROR"))
                .expectBody("isSuccess", equalTo(false))
                .build();
    }

}
