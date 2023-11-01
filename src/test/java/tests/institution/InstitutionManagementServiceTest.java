package tests.institution;

import endpoints.InstitutionEndpoints;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class InstitutionManagementServiceTest {
    Logger logger = LogManager.getLogger(this.getClass());
    String currentPage;
    String pageSize;


    @Test()
    public void listAdminsWithValidPageAndPageSize() {
        logger.info("Test case IMS_01 is running");
        currentPage = "1";
        pageSize = "10";
        String pagination = "{\"pagination\":{\"page\":" + currentPage + ",\"pageSize\":" + pageSize + "}}";
        Response response = InstitutionEndpoints.listAdmins(pagination);
        response.then()
                .log().body()
                .statusCode(200)
                .contentType("application/json")
                .body("response.content", hasSize(greaterThan(0)))
                .body("response.content[0].id", notNullValue())
                .body("response.content[0].username", notNullValue())
                .body("response.content[0].firstName", notNullValue())
                .body("response.content[0].lastName", notNullValue())
                .body("response.content[0].status", notNullValue())
                .body("response.pageNumber", notNullValue())
                .body("response.pageSize", notNullValue())
                .body("response.totalPageCount", notNullValue())
                .body("response.totalElementCount", notNullValue())
                .body("response", hasKey("sortedBy"))
                .body("response", hasKey("filteredBy"));
    }

    @Test()
    public void listAdminsWithEmptyPageValue() {
        logger.info("Test case IMS_02 is running");
        pageSize = "10";
        String pagination = "{\"pagination\":{\"page\":" + currentPage + ",\"pageSize\":" + pageSize + "}}";
        Response response = InstitutionEndpoints.listAdmins(pagination);
        response.then()
                .log().body()
                .statusCode(400)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("header", equalTo("VALIDATION ERROR"))
                .body("isSuccess", equalTo(false))
                .body("subErrors[0].message", equalTo("must not be null"))
                .body("subErrors[0].field", equalTo("page"))
                .body("subErrors[0].type", equalTo("Long"));
    }

    @Test()
    public void listAdminsWithEmptyPageSizeValue() {
        logger.info("Test case IMS_03 is running");
        currentPage = "1";
        String pagination = "{\"pagination\":{\"page\":" + currentPage + ",\"pageSize\":" + pageSize + "}}";
        Response response = InstitutionEndpoints.listAdmins(pagination);
        response.then()
                .log().body()
                .statusCode(400)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("header", equalTo("VALIDATION ERROR"))
                .body("isSuccess", equalTo(false))
                .body("subErrors[0].message", equalTo("must not be null"))
                .body("subErrors[0].field", equalTo("pageSize"))
                .body("subErrors[0].type", equalTo("Long"));
    }

    @Test()
    public void listAdminsWithMissingPageField() {
        logger.info("Test case IMS_04 is running");
        pageSize = "10";
        String pagination = "{\"pagination\":{\"pageSize\":" + pageSize + "}}";
        Response response = InstitutionEndpoints.listAdmins(pagination);
        response.then()
                .log().body()
                .statusCode(400)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("header", equalTo("VALIDATION ERROR"))
                .body("isSuccess", equalTo(false))
                .body("subErrors[0].message", equalTo("must not be null"))
                .body("subErrors[0].field", equalTo("page"))
                .body("subErrors[0].type", equalTo("Long"));
    }

    @Test()
    public void listAdminsWithMissingPageSizeField() {
        logger.info("Test case IMS_05 is running");
        currentPage = "1";
        String pagination = "{\"pagination\":{\"page\":" + currentPage + "}}";
        Response response = InstitutionEndpoints.listAdmins(pagination);
        response.then()
                .log().body()
                .statusCode(400)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("header", equalTo("VALIDATION ERROR"))
                .body("isSuccess", equalTo(false))
                .body("subErrors[0].message", equalTo("must not be null"))
                .body("subErrors[0].field", equalTo("pageSize"))
                .body("subErrors[0].type", equalTo("Long"));
    }

    @Test()
    public void listAdminsWithMissingPageAndPageSizeField() {
        logger.info("Test case IMS_06 is running");
        String pagination = "{\"pagination\":{}}";
        Response response = InstitutionEndpoints.listAdmins(pagination);
        response.then()
                .log().body()
                .statusCode(400)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("header", equalTo("VALIDATION ERROR"))
                .body("isSuccess", equalTo(false))
                .body("subErrors[0].message", equalTo("must not be null"))
                .body("subErrors[0].field", equalTo("pageSize"))
                .body("subErrors[0].type", equalTo("Long"))
                .body("subErrors[1].message", equalTo("must not be null"))
                .body("subErrors[1].field", equalTo("page"))
                .body("subErrors[1].type", equalTo("Long"));
    }

    @Test()
    public void listAdminsWithPageSizeValueNot10() {
        logger.info("Test case IMS_07 is running");
        currentPage = "1";
        pageSize = "4";
        String pagination = "{\"pagination\":{\"page\":" + currentPage + ",\"pageSize\":" + pageSize + "}}";
        Response response = InstitutionEndpoints.listAdmins(pagination);
        response.then()
                .log().body()
                .statusCode(400)
                .contentType("application/json")
                .body("time", notNullValue())
                .body("httpStatus", equalTo("BAD_REQUEST"))
                .body("header", equalTo("VALIDATION ERROR"))
                .body("isSuccess", equalTo(false))
                .body("subErrors[0].message", equalTo("must be between 10 and 10"))
                .body("subErrors[0].field", equalTo("pageSize"))
                .body("subErrors[0].type", equalTo("Long"));
    }


}
