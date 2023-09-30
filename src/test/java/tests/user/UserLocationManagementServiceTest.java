package tests.user;

import endpoints.UserEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utility.DatabaseUtility;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

public class UserLocationManagementServiceTest extends UserEndpoints{
    DatabaseUtility databaseUtility = new DatabaseUtility();

    @BeforeClass()
    public void beforeClass() {
        databaseUtility.connect();
        databaseUtility.updateUserStatus("IDLE");
        databaseUtility.updateAssignments();
        databaseUtility.disconnect();
    }
    @Test(priority = 0)
    public void assignmentSearchAsSupportStatusIDLE() {
        String payload = "{\n" +
                "    \"latitude\": 37.991739,\n" +
                "    \"longitude\": 27.024168\n" +
                "}";
        Response response = UserEndpoints.searchAssignment(payload);
        response.then()
                .statusCode(409)
                .body("time", notNullValue())
                .body("httpStatus", equalTo("CONFLICT"))
                .body("header", equalTo("ALREADY EXIST"))
                .body("message", containsString("USER NOT READY TO TAKE ASSIGNMENT!"))
                .body("isSuccess", equalTo(false));
    }
}
