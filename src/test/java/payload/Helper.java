package payload;

import com.github.javafaker.Faker;
import io.restassured.response.Response;

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
