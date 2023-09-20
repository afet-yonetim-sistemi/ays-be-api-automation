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
                .statusCode(200);
        response.then().contentType("application/json");
        response.then().body("response.content", hasSize(greaterThan(0)));

        response.then().body("response.content[0].id", notNullValue());
        response.then().body("response.content[0].username", notNullValue());
        response.then().body("response.content[0].firstName", notNullValue());
        response.then().body("response.content[0].lastName", notNullValue());
        response.then().body("response.content[0].status", notNullValue());

        response.then().body("response.pageNumber", notNullValue());
        response.then().body("response.pageSize", notNullValue());
        response.then().body("response.totalPageCount", notNullValue());
        response.then().body("response.totalElementCount", notNullValue());
        response.then().body("response", hasKey("sortedBy"));
        response.then().body("response", hasKey("filteredBy"));
    }

}
