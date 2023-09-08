package tests;

import endpoints.InstitutionEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class InstitutionManagementServiceTest extends InstitutionEndpoints {
    @Test(priority = 0)
    public void listAdmins() {
        int currentPage = 1;
        String pagination = "{\"pagination\":{\"page\":" + currentPage + ",\"pageSize\":10}}";
        Response response=InstitutionEndpoints.listAdmins(pagination);
        response.then()
                    .statusCode(200);
    }

}
