package tests.institution.adminregistrationmanagementservice;

import endpoints.InstitutionEndpoints;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import payload.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class PostAdminRegistrationApplicationsTest {

    Logger logger = LogManager.getLogger(this.getClass());
    RequestBodyInstitution requestBodyInstitution;
    Pagination pagination;
    FilterForInstitution filter;
    Sort sort;

    @BeforeMethod
    public void setup() {
        requestBodyInstitution = new RequestBodyInstitution();
        pagination = new Pagination();
        filter = new FilterForInstitution();
        sort = new Sort();
    }

    @Test()
    @DisplayName("Pagination with Authorization")
    public void postRegistrationWithPagination() {
        logger.info("Test case ARMS_01 is running.. ");
        pagination.setPage(1);
        pagination.setPageSize(10);
        requestBodyInstitution.setPagination(pagination);


        Response response = InstitutionEndpoints.postRegistrationApplications(requestBodyInstitution, "SUPER_ADMIN");
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("response.content", notNullValue())
                .body("response.sortedBy", nullValue());
    }

    @Test()
    @DisplayName("Pagination with Unauthorized")
    public void postRegistrationWithPaginationUnauthorized() {
        logger.info("Test case ARMS_02 is running.. ");
        pagination.setPage(1);
        pagination.setPageSize(10);
        requestBodyInstitution.setPagination(pagination);

        Response response = InstitutionEndpoints.postRegistrationApplications(requestBodyInstitution, "ADMIN");
        response.then()
                .statusCode(403)
                .contentType("application/json")
                .body("httpStatus", equalTo("FORBIDDEN"))
                .body("isSuccess", equalTo(false))
                .body("header", equalTo("AUTH ERROR"));

    }

    @Test(dataProvider = "paginationScenarios")
    @DisplayName("checking all invalid pagination values")
    public void postRegistrationWithPagination(int page,int pageSize) {
        logger.info("Test case ARMS_03 is running.. ");
        pagination.setPage(page);
        pagination.setPageSize(pageSize);
        requestBodyInstitution.setPagination(pagination);


        Response response = InstitutionEndpoints.postRegistrationApplications(requestBodyInstitution, "SUPER_ADMIN");
        response.then()
                .statusCode(500)
                .contentType("application/json")
                .body("httpStatus", equalTo("INTERNAL_SERVER_ERROR"))
                .body("isSuccess", equalTo(false))
                .body("header", equalTo("PROCESS ERROR"));
    }

    @Test()
    @DisplayName("Pagination and filter with Authorization")
    public void postRegistrationWithPaginationAndFilter() {
        logger.info("Test case ARMS_06 is running.. ");
        pagination.setPage(1);
        pagination.setPageSize(10);
        requestBodyInstitution.setPagination(pagination);

        List<String> newStatuses = new ArrayList<>();
        newStatuses.add("WAITING");
        filter.setStatuses(newStatuses);
        requestBodyInstitution.setFilterForInstitution(filter);

        Response response = InstitutionEndpoints.postRegistrationApplications(requestBodyInstitution, "SUPER_ADMIN");
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("response.content", notNullValue())
                .body("response.sortedBy", nullValue())
                .body("response.filteredBy",notNullValue());
    }

    @Test()
    @DisplayName("Pagination and filter with Unauthorized")
    public void postRegistrationWithPaginationAndFilter2() {
        logger.info("Test case ARMS_07 is running.. ");
        pagination.setPage(1);
        pagination.setPageSize(10);
        requestBodyInstitution.setPagination(pagination);

        List<String> newStatuses = Arrays.asList("WAITING");
        filter.setStatuses(newStatuses);
        requestBodyInstitution.setFilterForInstitution(filter);

        Response response = InstitutionEndpoints.postRegistrationApplications(requestBodyInstitution, "ADMIN");
        response.then()
                .statusCode(403)
                .contentType("application/json")
                .body("httpStatus", equalTo("FORBIDDEN"))
                .body("isSuccess", equalTo(false))
                .body("header", equalTo("AUTH ERROR"));
    }

    @Test()
    @DisplayName("Pagination and filter with invalid statuses value")
    public void postRegistrationWithPaginationAndFilter3() {
        logger.info("Test case ARMS_08 is running.. ");
        pagination.setPage(1);
        pagination.setPageSize(10);
        requestBodyInstitution.setPagination(pagination);
        List<String> statuses = new ArrayList<>();
        statuses.add("WAIT");
        filter.setStatuses(statuses);
        requestBodyInstitution.setFilterForInstitution(filter);
        Response response = InstitutionEndpoints.postRegistrationApplications(requestBodyInstitution, "SUPER_ADMIN");
        response.then()
                .log().body()
                .statusCode(400)
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("isSuccess", equalTo(false))
                .body("header", equalTo("VALIDATION ERROR"));
    }

    @Test()
    @DisplayName("Pagination and sort with Authorization")
    public void postRegistrationWithPaginationAndSort() {
        logger.info("Test case ARMS_09 is running.. ");
        pagination.setPage(1);
        pagination.setPageSize(10);
        requestBodyInstitution.setPagination(pagination);

        sort.setProperty("createdAt");
        sort.setDirection("ASC");
        List<Sort> newSort = new ArrayList<>();
        newSort.add(sort);
        requestBodyInstitution.setSort(newSort);

        Response response = InstitutionEndpoints.postRegistrationApplications(requestBodyInstitution, "SUPER_ADMIN");
        response.then()
                .log().body()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("response.content", notNullValue())
                .body("response.sortedBy", notNullValue())
                .body("response.filteredBy", nullValue());
    }

    @Test()
    @DisplayName("Pagination and sort with Unauthorization")
    public void postRegistrationWithPaginationAndSort2() {
        logger.info("Test case ARMS_10 is running.. ");
        pagination.setPage(1);
        pagination.setPageSize(10);
        requestBodyInstitution.setPagination(pagination);

        sort.setProperty("createdAt");
        sort.setDirection("ASC");
        List<Sort> newSort = new ArrayList<>();
        newSort.add(sort);
        requestBodyInstitution.setSort(newSort);

        Response response = InstitutionEndpoints.postRegistrationApplications(requestBodyInstitution, "ADMIN");
        response.then()
                .log().body()
                .statusCode(403)
                .contentType("application/json")
                .body("httpStatus", equalTo("FORBIDDEN"))
                .body("isSuccess", equalTo(false))
                .body("header", equalTo("AUTH ERROR"));
    }

    @Test()
    @DisplayName("Pagination,sort and filter with Authorization")
    public void postRegistrationWithPaginationSortFilter() {
        logger.info("Test case ARMS_011 is running.. ");
        pagination.setPage(1);
        pagination.setPageSize(10);
        requestBodyInstitution.setPagination(pagination);

        List<String> statuses = new ArrayList<>();
        statuses.add("WAITING");
        filter.setStatuses(statuses);
        requestBodyInstitution.setFilterForInstitution(filter);

        sort.setProperty("createdAt");
        sort.setDirection("ASC");
        List<Sort> newSort = new ArrayList<>();
        newSort.add(sort);
        requestBodyInstitution.setSort(newSort);

        Response response = InstitutionEndpoints.postRegistrationApplications(requestBodyInstitution, "SUPER_ADMIN");
        response.then()
                .log().body()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("response.content", notNullValue())
                .body("response.sortedBy", notNullValue());
    }

    @DataProvider(name = "paginationScenarios") //ARMS_03, ARMS_04, ARMS_05
    public Object[][] paginationScenarios() {
        return new Object[][]{
                {0, 10},
                {1, 0},
                {1, 100},
                {-1, 10},
                {1, -1},
                {-1, -1}
        };

    }
}