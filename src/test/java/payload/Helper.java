package payload;

import com.github.javafaker.Faker;
import endpoints.InstitutionEndpoints;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Helper {

    public static Assignment createRandomAssignment() {
        int MIN_LATITUDE = 35;
        int MAX_LATITUDE = 42;
        int MIN_LONGITUDE = 26;
        int MAX_LONGITUDE = 45;

        Faker faker = new Faker();
        Assignment assignment = new Assignment();
        assignment.setFirstName(faker.name().firstName());
        assignment.setLastName(faker.name().lastName());

        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setLineNumber(generateLineNumber());
        phoneNumber.setCountryCode("90");
        assignment.setPhoneNumber(phoneNumber);

        assignment.setDescription(faker.commerce().productName());
        assignment.setLatitude(faker.number().randomDouble(6, MIN_LATITUDE, MAX_LATITUDE));
        assignment.setLongitude(faker.number().randomDouble(6, MIN_LONGITUDE, MAX_LONGITUDE));

        return assignment;
    }

    public static UserCredentials createNewUser() {
        Faker faker;
        User userPayload;
        PhoneNumber phoneNumber;
        faker = new Faker();
        userPayload = new User();
        phoneNumber = new PhoneNumber();
        userPayload.setFirstName(faker.name().firstName());
        userPayload.setLastName(faker.name().lastName());
        phoneNumber.setLineNumber(generateLineNumber());
        phoneNumber.setCountryCode("90");
        userPayload.setPhoneNumber(phoneNumber);
        Response response = InstitutionEndpoints.createAUser(userPayload);

        if (response.getStatusCode() == 200) {
            return response.then()
                    .extract().jsonPath().getObject("response", UserCredentials.class);
        } else {
            throw new RuntimeException("User creation failed with status code: " + response.getStatusCode());
        }
    }

    public static Map<String, String> createLoginPayload(String username, String password) {
        Map<String, String> payload = new HashMap<>();
        payload.put("username", username);
        payload.put("password", password);
        return payload;
    }

    public static RequestBodyAssignments createRequestBodyAssignments() {
        RequestBodyAssignments requestBodyAssignments = new RequestBodyAssignments();
        Pagination pagination = new Pagination();
        pagination.setPage(1);
        pagination.setPageSize(10);
        requestBodyAssignments.setPagination(pagination);
        return requestBodyAssignments;
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

    public static String extractIdFromListAssignments(Response response) {
        String id = null;
        String latestCreatedAt = "";
        List<Map<String, Object>> content = response.jsonPath().getList("response.content");
        for (Map<String, Object> assignment : content) {
            String createdAt = (String) assignment.get("createdAt");
            if (createdAt != null && createdAt.compareTo(latestCreatedAt) > 0) {
                latestCreatedAt = createdAt;
                id = (String) assignment.get("id");
            }
        }
        return id;
    }
}
