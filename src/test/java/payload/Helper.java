package payload;

import com.github.javafaker.Faker;
import endpoints.InstitutionEndpoints;
import endpoints.UserEndpoints;
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
    public static User getUser(String userID){
        Response response = InstitutionEndpoints.getUser(userID);
        if (response.getStatusCode() == 200) {
            if (response.jsonPath().get("response") != null) {
                return response.then()
                        .extract()
                        .jsonPath()
                        .getObject("response", User.class);
            } else {
                throw new RuntimeException("User data not found for userID: " + userID);
            }
        } else {
            throw new RuntimeException("Failed to retrieve user with status code: " + response.getStatusCode());
        }
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

    public static RequestBodyAssignments createRequestBodyAssignments(String page,String pageSize) {
        RequestBodyAssignments requestBodyAssignments = new RequestBodyAssignments();
        Pagination pagination = new Pagination();
        pagination.setPage(page);
        pagination.setPageSize(pageSize);
        requestBodyAssignments.setPagination(pagination);
        return requestBodyAssignments;
    }
    public static Assignment createANewAssignment(){
        Faker faker=new Faker();
        Assignment assignment=new Assignment();
        assignment.setFirstName(faker.name().firstName());
        assignment.setLastName(faker.name().lastName());
        assignment.setPhoneNumber(createPhoneNumber());
        assignment.setDescription(faker.commerce().productName());
        assignment.setLatitude(generateRandomCoordinate(38, 40));
        assignment.setLongitude(generateRandomCoordinate(28, 43));
        InstitutionEndpoints.createAnAssignment(assignment);
        return assignment;
    }
    public static PhoneNumber createPhoneNumber(){
        PhoneNumber phoneNumber=new PhoneNumber();
        phoneNumber.setLineNumber(generateLineNumber());
        phoneNumber.setCountryCode("90");
        return phoneNumber;


    }

    public static RequestBodyAssignments createRequestBodyAssignmentsMissingPagination() {
        RequestBodyAssignments requestBodyAssignments = new RequestBodyAssignments();
        Pagination pagination = new Pagination();
        requestBodyAssignments.setPagination(pagination);
        return requestBodyAssignments;
    }

    public static String generateLineNumber() {
        Random random = new Random();
        StringBuilder phoneNumberBuilder = new StringBuilder();
        phoneNumberBuilder.append("535");
        for (int i = 0; i < 7; i++) {
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

    public static String getUserID(String username) {
        String pagination = "{\"pagination\":{\"page\":1,\"pageSize\":10}}";
        int currentPage = 1;

        while (true) {
            Response response = InstitutionEndpoints.listUsers(pagination);
            String userID = extractUserIdFromTheUserList(response, username);

            if (userID != null) {
                return userID;
            }

            int currentPageNumber = response.jsonPath().getInt("response.pageNumber");
            int totalPageCount = response.jsonPath().getInt("response.totalPageCount");

            if (currentPageNumber >= totalPageCount) {
                break;
            }
            pagination = "{\"pagination\":{\"page\":" + (currentPage + 1) + ",\"pageSize\":10}}";
            currentPage++;
        }

        return null;
    }

    private static String extractUserIdFromTheUserList(Response response, String username) {
        List<Map<String, Object>> userList = response.jsonPath().getList("response.content");
        for (Map<String, Object> user : userList) {
            String userUsername = (String) user.get("username");
            if (username.equals(userUsername)) {
                return (String) user.get("id");
            }
        }
        return null;
    }

    public static Location generateLocation(double minLat, double maxLat, double minLon, double maxLon) {
        Random rand = new Random();
        double latitude = minLat + (maxLat - minLat) * rand.nextDouble();
        double longitude = minLon + (maxLon - minLon) * rand.nextDouble();
        Location location = new Location();
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }

    public static void setSupportStatus(String status, String username, String password) {
        String payload = createPayloadWithSupportStatus(status);
        UserEndpoints.updateSupportStatus(payload, username, password);
    }

    public static String createPayloadWithSupportStatus(String supportStatus) {
        return "{\n" +
                "    \"supportStatus\": \"" + supportStatus + "\"\n" +
                "}";
    }
    private static double generateRandomCoordinate(int min, int max) {
        Random random = new Random();
        return min + (max - min) * random.nextDouble();
    }
}

