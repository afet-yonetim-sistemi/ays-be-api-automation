package tests.institution;

import com.github.javafaker.Faker;
import endpoints.InstitutionEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import payload.PhoneNumber;
import payload.User;
import payload.UserCredentials;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.hamcrest.Matchers.*;

public class UserManagementServiceTest extends InstitutionEndpoints {
    Faker faker;
    User userPayload;
    PhoneNumber phoneNumber;
    String userId;
    UserCredentials userCredentials;

    @BeforeClass
    public void setupData() {
        faker = new Faker();
        userPayload = new User();
        phoneNumber = new PhoneNumber();
        userPayload.setFirstName(faker.name().firstName());
        userPayload.setLastName(faker.name().lastName());
        phoneNumber.setLineNumber(generateLineNumber());
        userPayload.setPhoneNumber(phoneNumber);
    }

    @Test(priority = 0)
    public void createAUser() {
        Response response = InstitutionEndpoints.createAUser(userPayload);
        userCredentials = response.then()
                .statusCode(200)
                .extract().jsonPath().getObject("response", UserCredentials.class);
        response.then()
                .contentType("application/json")
                .body("response", notNullValue())
                .body("response.username", notNullValue())
                .body("response.password", notNullValue());
    }

    @Test(priority = 1)
    public void listUsers() {
        int currentPage = 1;
        int totalPageCount = Integer.MAX_VALUE;

        while (currentPage <= totalPageCount) {
            String pagination = "{\"pagination\":{\"page\":" + currentPage + ",\"pageSize\":10}}";
            Response response = InstitutionEndpoints.listUsers(pagination);
            response.then().statusCode(200)
                    .contentType("application/json")
                    .body("httpStatus", equalTo("OK"))
                    .body("isSuccess", equalTo(true))
                    .body("response.content", notNullValue())
                    .body("response.content", hasSize(greaterThan(0)))
                    .body("response.content[0].id", notNullValue())
                    .body("response.content[0].username", notNullValue())
                    .body("response.content[0].firstName", notNullValue())
                    .body("response.content[0].lastName", notNullValue())
                    .body("response.content[0].role", notNullValue())
                    .body("response.content[0].status", notNullValue())
                    .body("response.content[0].institution", notNullValue())
                    .body("response.pageNumber", notNullValue())
                    .body("response.pageSize", notNullValue())
                    .body("response.totalPageCount", notNullValue())
                    .body("response.totalElementCount", notNullValue())
                    .body("response.sortedBy", nullValue())
                    .body("response.filteredBy", nullValue());

            extractUserIdFromTheUserList(response);

            int currentPageNumber = response.jsonPath().getInt("response.pageNumber");
            totalPageCount = response.jsonPath().getInt("response.totalPageCount");

            if (currentPageNumber >= totalPageCount) {
                break;
            }
            currentPage++;
        }
    }

    @Test(priority = 2)
    public void getUser() {
        Response response = InstitutionEndpoints.getUser(userId);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("response.createdUser", notNullValue())
                .body("response.id", notNullValue())
                .body("response.username", notNullValue())
                .body("response.firstName", notNullValue())
                .body("response.lastName", notNullValue())
                .body("response", hasKey("email"))
                .body("response.role", notNullValue())
                .body("response.status", notNullValue())
                .body("response.phoneNumber.countryCode", notNullValue())
                .body("response.phoneNumber.lineNumber", notNullValue())
                .body("response.supportStatus", notNullValue())
                .body("response.institution.createdUser", notNullValue())
                .body("response.institution.createdAt", notNullValue())
                .body("response.institution.updatedUser", anyOf(equalTo(null), equalTo(String.class)))
                .body("response.institution.updatedAt", anyOf(equalTo(null), equalTo(String.class)))
                .body("response.institution.id", notNullValue())
                .body("response.institution.name", notNullValue());
    }

    @Test(priority = 3)
    public void updateUserAsPassive() {
        userPayload.setStatus("PASSIVE");
        userPayload.setRole("VOLUNTEER");
        Response response = InstitutionEndpoints.updateUser(userId, userPayload);
        response.then()
                .statusCode(200).contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));

    }

    @Test(priority = 4)
    public void updateUserAsActive() {
        userPayload.setRole("VOLUNTEER");
        userPayload.setStatus("ACTIVE");
        Response response = InstitutionEndpoints.updateUser(userId, userPayload);
        response.then()
                .statusCode(200).contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
    }

    @Test(priority = 5)
    public void deleteUser() {
        Response response = InstitutionEndpoints.deleteUser(userId);
        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("httpStatus", equalTo("OK"))
                .body("isSuccess", equalTo(true));
    }

    @Test(priority = 6)
    public void getUserAfterDelete() {
        Response response = InstitutionEndpoints.getUser(userId);
        response.then()
                .statusCode(200)
                .body("response.status", equalTo("DELETED"));
    }

    @Test(priority = 7)
    public void deleteUserNegative() {
        Response response = InstitutionEndpoints.deleteUser(userId);
        response.then()
                .statusCode(409)
                .body("isSuccess", equalTo(false))
                .body("header", containsString("ALREADY EXIST"))
                .body("message", containsString("USER IS ALREADY DELETED!"));

    }

    public static String generateLineNumber() {
        Random random = new Random();
        StringBuilder phoneNumberBuilder = new StringBuilder();
        phoneNumberBuilder.append("5");
        for (int i = 0; i < 9; i++) {
            int digit = random.nextInt(10);
            phoneNumberBuilder.append(digit);
        }
        return phoneNumberBuilder.toString();
    }

    private void extractUserIdFromTheUserList(Response response) {
        List<Map<String, Object>> userList = response.jsonPath().getList("response.content");

        for (Map<String, Object> user : userList) {
            String userUsername = (String) user.get("username");
            if (userUsername.equals(userCredentials.getUsername())) {
                userId = (String) user.get("id");
                break;
            }
        }
    }


}
