package tests.institution.usermanagementservice;

import endpoints.InstitutionEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import payload.Helper;
import payload.Pagination;
import payload.RequestBodyAssignments;


import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class PostUsersTest {
    RequestBodyAssignments requestBodyAssignments=new RequestBodyAssignments();

    @BeforeMethod
    public void setup(){
        Helper.createNewUser();
        requestBodyAssignments=Helper.createRequestBodyAssignments(1,10);
    }
    @Test
    public void listUsersPositive(){
        Response response=InstitutionEndpoints.listUsers(requestBodyAssignments);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true))
                .body("response.content", notNullValue());

    }
}
