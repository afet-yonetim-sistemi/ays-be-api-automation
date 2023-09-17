package tests;

import com.github.javafaker.Faker;
import endpoints.InstitutionEndpoints;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import payload.PhoneNumber;
import payload.User;
import payload.UserCredentials;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

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


    /**
     * This method sends a POST request to create a new user.
     * It generates random user information such as full name, last name, and phone number,
     * constructs a JSON request body with this information,
     * sends the request, and extracts the username from the response.
     */
    @Test(priority = 0)
    public void createAUser() {
        Response response = InstitutionEndpoints.createAUser(userPayload);
        userCredentials = response.then()
                .statusCode(200)
                .extract().jsonPath().getObject("response", UserCredentials.class);
    }

    /**
     * This method lists all users by sending POST requests to retrieve users in pages.
     * It iterates through all pages, extracts the user ID for a specific username to use later in other tests.
     * This is done because the API has paginated results.
     */
    @Test(priority = 1)
    public void listUsers() {
        int currentPage = 1;
        int totalPageCount = Integer.MAX_VALUE;
        String pagination = "{\"pagination\":{\"page\":" + currentPage + ",\"pageSize\":10}}";
        userId = null;
        while (currentPage <= totalPageCount) {
            Response response = InstitutionEndpoints.listUsers(pagination);
            response.then()
                    .statusCode(200);
            int currentPageNumber = response.jsonPath().getInt("response.pageNumber");
            totalPageCount = response.jsonPath().getInt("response.totalPageCount");
            extractUserIdFromTheUserList(response);
            currentPage++;
            if (currentPageNumber >= totalPageCount) {
                break;
            }
        }
        System.out.println(userId);
    }

    /**
     * This method retrieves a specific user's information by sending a GET request using the previously extracted userId.
     */
    @Test(priority = 2)
    public void getUser() {
        System.out.println("get user test userID:" + userId);
        Response response = InstitutionEndpoints.getUser(this.userId);
        response.then()
                .statusCode(200);
    }

    /**
     * Sends a PUT request to update the user's status to PASSIVE
     */
    @Test(priority = 3)
    public void updateUserAsPassive() {
        userPayload.setStatus("PASSIVE");
        userPayload.setRole("VOLUNTEER");
        Response response = InstitutionEndpoints.updateUser(userId, userPayload);
        response.then()
                .statusCode(200);

    }

    /**
     * Sends a PUT request to update the user's status to ACTIVE
     */
    @Test(priority = 4)
    public void updateUserAsActive() {
        userPayload.setRole("VOLUNTEER");
        userPayload.setStatus("ACTIVE");
        Response response = InstitutionEndpoints.updateUser(userId, userPayload);
        response
                .then()
                .statusCode(200);
    }

    /**
     * Sends a DELETE request to delete a specific user
     */
    @Test(priority = 5)
    public void deleteUser() {

        Response response = InstitutionEndpoints.deleteUser(userId);
        response.then()
                .statusCode(200)
                .log().body();
    }

    /**
     * Retrieves a user's information after deletion and checks if the status is DELETED
     */
    @Test(priority = 6)
    public void getUserAfterDelete() {
        System.out.println("after delete get user test userID:" + userId);
        Response response = InstitutionEndpoints.getUser(userId);
        response.then()
                .statusCode(200)
                .body("response.status", equalTo("DELETED"));
    }

    /**
     * Sends a DELETE request to delete a user that has already been deleted,
     * expecting a conflict (status code 409) and checking for a specific error message.
     */
    @Test(priority = 7)
    public void deleteUserNegative() {
        Response response = InstitutionEndpoints.deleteUser(userId);
        response.then()
                .statusCode(409)
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
        System.out.println(phoneNumberBuilder);
        return phoneNumberBuilder.toString();
    }

    private String generateRandomLastName() {
        return RandomStringUtils.randomAlphabetic(5);
    }

    private String generateRandomFullName() {
        return RandomStringUtils.randomAlphabetic(8);
    }

    private void extractUserIdFromTheUserList(Response listUsersResponse) {
        List<Map<String, Object>> userList = listUsersResponse.jsonPath().getList("response.content");
        for (Map<String, Object> user : userList) {
            String userUsername = (String) user.get("username");
            if (userUsername.equals(userCredentials.getUsername())) {
                userId = (String) user.get("id");
                break;
            }
        }
        System.out.println(userId);
    }

}
