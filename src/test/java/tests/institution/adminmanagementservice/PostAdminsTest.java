package tests.institution.adminmanagementservice;

import endpoints.InstitutionEndpoints;
import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;

@Epic("Institution Admin Management Service")
public class PostAdminsTest {
    String currentPage;
    String currentPageSize;

    @BeforeMethod
    public void setup() {
        currentPage = null;
        currentPageSize = null;
    }

    @Test(dataProvider = "positivePaginationData")
    @Story("As an Institution admin I want to list all admins")
    @Severity(SeverityLevel.NORMAL)
    public void listAdminsWithValidPageAndPageSize(int page, int pageSize) {
        String pagination = "{\"pagination\":{\"page\":" + page + ",\"pageSize\":" + pageSize + "}}";
        Response response = InstitutionEndpoints.listAdmins(pagination);
        if (page == 1 && pageSize == 10) {
            response.then()
                    .spec(successResponseSpec())
                    .body("response.content", hasSize(greaterThan(0)))
                    .body("response.content[0].id", notNullValue())
                    .body("response.content[0].username", notNullValue())
                    .body("response.content[0].firstName", notNullValue())
                    .body("response.content[0].lastName", notNullValue())
                    .body("response.content[0].status", notNullValue())
                    .body("response.pageNumber", equalTo(page))
                    .body("response.totalPageCount", notNullValue())
                    .body("response.totalElementCount", notNullValue())
                    .body("response", hasKey("sortedBy"))
                    .body("response", hasKey("filteredBy"));
        } else {
            response.then()
                    .spec(successResponseSpec())
                    .body("response.content", instanceOf(List.class))
                    .body("response.pageNumber", equalTo(page))
                    .body("response.totalPageCount", notNullValue())
                    .body("response.totalElementCount", notNullValue())
                    .body("response", hasKey("sortedBy"))
                    .body("response", hasKey("filteredBy"));
        }
    }

    @Test()
    @Story("As an Institution admin I want to get proper error message when page value is null")
    @Severity(SeverityLevel.NORMAL)
    public void listAdminsWithEmptyPageValue() {
        currentPageSize = "10";
        String pagination = "{\"pagination\":{\"page\":" + currentPage + ",\"pageSize\":" + currentPageSize + "}}";
        Response response = InstitutionEndpoints.listAdmins(pagination);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must be between 1 and 99999999"))
                .body("subErrors[0].field", equalTo("page"))
                .body("subErrors[0].type", equalTo("int"));
    }

    @Test()
    @Story("As an Institution admin I want to get proper error message when pageSize value is null")
    @Severity(SeverityLevel.NORMAL)
    public void listAdminsWithEmptyPageSizeValue() {
        currentPage = "1";
        String pagination = "{\"pagination\":{\"page\":" + currentPage + ",\"pageSize\":" + currentPageSize + "}}";
        Response response = InstitutionEndpoints.listAdmins(pagination);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must be between 1 and 99999999"))
                .body("subErrors[0].field", equalTo("pageSize"))
                .body("subErrors[0].type", equalTo("int"));
    }

    @Test()
    @Story("As an Institution admin I want to get proper error message when page field is missing")
    @Severity(SeverityLevel.NORMAL)
    public void listAdminsWithMissingPageField() {
        currentPageSize = "10";
        String pagination = "{\"pagination\":{\"pageSize\":" + currentPageSize + "}}";
        Response response = InstitutionEndpoints.listAdmins(pagination);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must be between 1 and 99999999"))
                .body("subErrors[0].field", equalTo("page"))
                .body("subErrors[0].type", equalTo("int"));
    }

    @Test()
    @Story("As an Institution admin I want to get proper error message when pageSize field is missing")
    @Severity(SeverityLevel.NORMAL)
    public void listAdminsWithMissingPageSizeField() {
        currentPage = "1";
        String pagination = "{\"pagination\":{\"page\":" + currentPage + "}}";
        Response response = InstitutionEndpoints.listAdmins(pagination);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must be between 1 and 99999999"))
                .body("subErrors[0].field", equalTo("pageSize"))
                .body("subErrors[0].type", equalTo("int"));
    }

    @Test()
    @Story("As an Institution admin I want to get proper error message when page and pageSize fields are missing")
    @Severity(SeverityLevel.NORMAL)
    public void listAdminsWithMissingPageAndPageSizeField() {
        String pagination = "{\"pagination\":{}}";
        Response response = InstitutionEndpoints.listAdmins(pagination);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must be between 1 and 99999999"))
                .body("subErrors[0].field", anyOf(equalTo("page"), equalTo("pageSize")))
                .body("subErrors[0].type", equalTo("int"))
                .body("subErrors[1].message", equalTo("must be between 1 and 99999999"))
                .body("subErrors[1].field", anyOf(equalTo("page"), equalTo("pageSize")))
                .body("subErrors[1].type", equalTo("int"));
    }

    @Test(dataProvider = "negativePaginationData")
    @Story("As an Institution admin I want to get proper error message when page or pageSize fields are invalid")
    @Severity(SeverityLevel.NORMAL)
    public void listAdminsWithNegativeScenarios(int page, int pageSize) {
        String pagination = "{\"pagination\":{\"page\":" + page + ",\"pageSize\":" + pageSize + "}}";
        Response response = InstitutionEndpoints.listAdmins(pagination);
        response.then()
                .spec(badRequestResponseSpec())
                .body("subErrors[0].message", equalTo("must be between 1 and 99999999"))
                .body("subErrors[0].field", anyOf(equalTo("page"), equalTo("pageSize")))
                .body("subErrors[0].type", equalTo("int"));
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

    private ResponseSpecification successResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType("application/json")
                .expectBody("time", notNullValue())
                .expectBody("httpStatus", equalTo("OK"))
                .expectBody("isSuccess", equalTo(true))
                .build();
    }

    @DataProvider(name = "negativePaginationData")
    public static Object[][] negativePaginationData() {
        return new Object[][]{
                {-1, 10},
                {100000000, 10},
                {-5, 10},
                {1, -10},
                {1, 1000000000},
                {1, -5},
                {-100, 100000000},
                {-5, -5},
                {100000000, 100000000}
        };
    }

    @DataProvider(name = "positivePaginationData")
    public static Object[][] positivePaginationData() {
        return new Object[][]{
                {1, 10},
                {99999999, 1},
                {50, 50},
                {1000, 20}
        };
    }

}
