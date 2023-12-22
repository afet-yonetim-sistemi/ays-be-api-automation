package tests.institution.adminregistrationmanagementservice;

import endpoints.InstitutionEndpoints;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.testng.annotations.Test;
import payload.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class PostAdminRegistrationApplicationsTest {

    RequestBodyInstitution requestBodyInstitution = new RequestBodyInstitution();
    Pagination pagination = new Pagination();
    FilterForInstitution filter = new FilterForInstitution();
    Sort sort = new Sort();
    Logger logger = LogManager.getLogger(this.getClass());

    @Test()
    @DisplayName("Pagination with valid role")
    public void postRegistrationWithPagination() {
        logger.info("Test case ARMS_01 is running.. ");
        pagination.setPage(1);
        pagination.setPageSize(10);
        requestBodyInstitution.setPagination(pagination);


        Response response = InstitutionEndpoints.postRegistrationApplications(requestBodyInstitution);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("response.content", notNullValue())
                .body("response.sortedBy", nullValue());
    }

    @Test()
    @DisplayName("Pagination with invalid role")
    public void postRegistrationWithPaginationInvalidRole() {
        logger.info("Test case ARMS_02 is running.. ");
        pagination.setPage(1);
        pagination.setPageSize(10);
        requestBodyInstitution.setPagination(pagination);


        Response response = InstitutionEndpoints.listAdmins(requestBodyInstitution);
        response.then()
                .statusCode(403)
                .contentType("application/json")
                .body("httpStatus", equalTo("FORBIDDEN"))
                .body("isSuccess", equalTo(false))
                .body("header", equalTo("AUTH ERROR"));

    }

    @Test()
    @DisplayName("Pagination with invalid pageSize value")
    public void postRegistrationWithPagination2() {
        logger.info("Test case ARMS_03 is running.. ");
        pagination.setPage(1);
        pagination.setPageSize(-1);
        requestBodyInstitution.setPagination(pagination);


        Response response = InstitutionEndpoints.postRegistrationApplications(requestBodyInstitution);
        response.then()
                .statusCode(500)
                .contentType("application/json")
                .body("httpStatus", equalTo("INTERNAL_SERVER_ERROR"))
                .body("isSuccess", equalTo(false))
                .body("header", equalTo("PROCESS ERROR"));
    }

    @Test()
    @DisplayName("Pagination with invalid page value")
    public void postRegistrationWithPagination3() {
        logger.info("Test case ARMS_04 is running.. ");
        pagination.setPage(-1);
        pagination.setPageSize(10);
        requestBodyInstitution.setPagination(pagination);


        Response response = InstitutionEndpoints.postRegistrationApplications(requestBodyInstitution);
        response.then()
                .statusCode(500)
                .contentType("application/json")
                .body("httpStatus", equalTo("INTERNAL_SERVER_ERROR"))
                .body("isSuccess", equalTo(false))
                .body("header", equalTo("PROCESS ERROR"));
    }

    @Test()
    @DisplayName("Pagination with invalid page and pageSize value")
    public void postRegistrationWithPagination4() {
        logger.info("Test case ARMS_05 is running.. ");
        pagination.setPage(-1);
        pagination.setPageSize(-1);
        requestBodyInstitution.setPagination(pagination);


        Response response = InstitutionEndpoints.postRegistrationApplications(requestBodyInstitution);
        response.then()
                .statusCode(500)
                .contentType("application/json")
                .body("httpStatus", equalTo("INTERNAL_SERVER_ERROR"))
                .body("isSuccess", equalTo(false))
                .body("header", equalTo("PROCESS ERROR"));
    }

    @Test()
    @DisplayName("Pagination and filter with valid role")
    public void postRegistrationWithPaginationAndFilter() {
        logger.info("Test case ARMS_06 is running.. ");
        pagination.setPage(1);
        pagination.setPageSize(10);
        requestBodyInstitution.setPagination(pagination);

        List<String> newStatuses = Arrays.asList("WAITING");
        filter.setStatuses(newStatuses);
        requestBodyInstitution.setFilterForInstitution(filter);

        Response response = InstitutionEndpoints.postRegistrationApplications(requestBodyInstitution);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("response.content", notNullValue())
                .body("response.sortedBy", nullValue());
    }

    @Test()
    @DisplayName("Pagination and filter with invalid role")
    public void postRegistrationWithPaginationAndFilter2() {
        logger.info("Test case ARMS_07 is running.. ");
        pagination.setPage(1);
        pagination.setPageSize(10);
        requestBodyInstitution.setPagination(pagination);

        List<String> newStatuses = Arrays.asList("WAITING");
        filter.setStatuses(newStatuses);
        requestBodyInstitution.setFilterForInstitution(filter);

        Response response = InstitutionEndpoints.listAdmins(requestBodyInstitution);
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

        Response response = InstitutionEndpoints.postRegistrationApplications(requestBodyInstitution);
        response.then()
                .statusCode(400)
                .contentType("application/json")
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("isSuccess", equalTo(false))
                .body("header", equalTo("VALIDATION ERROR"));
    }

    @Test()
    @DisplayName("Pagination and sort with valid role")
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

        Response response = InstitutionEndpoints.postRegistrationApplications(requestBodyInstitution);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("response.content", notNullValue())
                .body("response.sortedBy", nullValue());
    }

}