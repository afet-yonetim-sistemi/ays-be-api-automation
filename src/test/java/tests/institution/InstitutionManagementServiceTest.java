package tests.institution;

import endpoints.InstitutionEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class InstitutionManagementServiceTest extends InstitutionEndpoints {
    @Test()
    public void listAdmins() {
        int currentPage = 1;
        String pagination = "{\"pagination\":{\"page\":" + currentPage + ",\"pageSize\":10}}";
        Response response = InstitutionEndpoints.listAdmins(pagination);
        response.then()
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

}
