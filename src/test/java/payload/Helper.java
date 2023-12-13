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
    public static User getUser(String userID) {
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
        User user = createUserPayload();
        Response response = InstitutionEndpoints.createAUser(user);
        if (response.getStatusCode() == 200) {
            return response.then()
                    .extract().jsonPath().getObject("response", UserCredentials.class);
        } else {
            throw new RuntimeException("User creation failed with status code: " + response.getStatusCode());
        }
    }

    public static UserCredentials createNewUser(User userPayload) {
        Response response = InstitutionEndpoints.createAUser(userPayload);
        if (response.getStatusCode() == 200) {
            return response.then()
                    .extract().jsonPath().getObject("response", UserCredentials.class);
        } else {
            throw new RuntimeException("User creation failed with status code: " + response.getStatusCode());
        }
    }

    public static User createUserPayload() {
        Faker faker;
        User userPayload;
        faker = new Faker();
        userPayload = new User();
        userPayload.setFirstName(faker.name().firstName());
        userPayload.setLastName(faker.name().lastName());
        userPayload.setPhoneNumber(createPhoneNumber());
        return userPayload;
    }

    public static Map<String, String> createLoginPayload(String username, String password) {
        Map<String, String> payload = new HashMap<>();
        payload.put("username", username);
        payload.put("password", password);
        return payload;
    }

    public static RequestBodyAssignments createRequestBodyAssignments(int page, int pageSize) {
        RequestBodyAssignments requestBodyAssignments = new RequestBodyAssignments();
        Pagination pagination = new Pagination();
        pagination.setPage(page);
        pagination.setPageSize(pageSize);
        requestBodyAssignments.setPagination(pagination);
        return requestBodyAssignments;
    }

    public static Assignment createANewAssignment() {
        Faker faker = new Faker();
        Assignment assignment = new Assignment();
        assignment.setFirstName(faker.name().firstName());
        assignment.setLastName(faker.name().lastName());
        assignment.setPhoneNumber(createPhoneNumber());
        assignment.setDescription(faker.commerce().productName());
        assignment.setLatitude(generateRandomCoordinate(38, 40));
        assignment.setLongitude(generateRandomCoordinate(28, 43));
        InstitutionEndpoints.createAnAssignment(assignment);
        return assignment;
    }

    public static PhoneNumber createPhoneNumber() {
        PhoneNumber phoneNumber = new PhoneNumber();
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

    public static String extractUserIdByDescriptionAndName(String description, String firstName, String lastName) {
        int currentPage = 1;
        int totalPageCount = Integer.MAX_VALUE;

        while (currentPage <= totalPageCount) {
            Response assignmentResponse = InstitutionEndpoints.listAssignments(createRequestBodyAssignments(currentPage, 10));
            totalPageCount = assignmentResponse.jsonPath().getInt("response.totalPageCount");

            String assignmentId = findAssignmentIdInContent(assignmentResponse, description, firstName, lastName);
            if (assignmentId != null) {
                return assignmentId;
            }

            currentPage++;
            if (currentPage > totalPageCount) {
                break;
            }
        }
        return null;
    }

    private static String findAssignmentIdInContent(Response response, String description, String firstName, String lastName) {
        List<Map<String, Object>> content = response.jsonPath().getList("response.content");

        for (Map<String, Object> entry : content) {
            String desc = (String) entry.get("description");
            String fName = (String) entry.get("firstName");
            String lName = (String) entry.get("lastName");

            if (desc != null && desc.equals(description) && fName != null && fName.equals(firstName) && lName != null && lName.equals(lastName)) {
                return (String) entry.get("id");
            }
        }
        return null;
    }


    public static String getUserID(String username) {
        int currentPage = 1;

        while (true) {
            Pagination pagination = setPagination(currentPage, 10);
            RequestBodyAssignments requestBodyAssignments = new RequestBodyAssignments();
            requestBodyAssignments.setPagination(pagination);
            Response response = InstitutionEndpoints.listUsers(requestBodyAssignments);
            response.then().log().body();
            String userID = extractUserIdFromTheUserList(response, username);
            if (userID != null) {
                return userID;
            }
            int currentPageNumber = response.jsonPath().getInt("response.pageNumber");
            int totalPageCount = response.jsonPath().getInt("response.totalPageCount");
            if (currentPageNumber >= totalPageCount) {
                break;
            }
            currentPage++;
        }

        return null;
    }

    public static String getUserIDByFirstName(String firstName) {
        RequestBodyAssignments requestBodyAssignments = new RequestBodyAssignments();
        Pagination pagination = setPagination(1, 10);
        requestBodyAssignments.setPagination(setPagination(1, 10));
        int currentPage = 1;
        while (true) {
            Response response = InstitutionEndpoints.listUsers(requestBodyAssignments);
            String userID = extractUserIdFromTheUserListByFirstname(response, firstName);
            if (userID != null) {
                return userID;
            }
            int currentPageNumber = response.jsonPath().getInt("response.pageNumber");
            int totalPageCount = response.jsonPath().getInt("response.totalPageCount");
            if (currentPageNumber >= totalPageCount) {
                break;
            }
            pagination.setPage(currentPage + 1);
            requestBodyAssignments.setPagination(pagination);
            currentPage++;
        }

        return null;
    }

    public static Pagination setPagination(int page, int pageSize) {
        Pagination pagination = new Pagination();
        pagination.setPageSize(pageSize);
        pagination.setPage(page);
        return pagination;
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

    private static String extractUserIdFromTheUserListByFirstname(Response response, String firstname) {
        List<Map<String, Object>> userList = response.jsonPath().getList("response.content");
        for (Map<String, Object> user : userList) {
            String userFirstName = (String) user.get("firstName");
            if (firstname.equals(userFirstName)) {
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

    public static Location generateLocationTR() {
        Random rand = new Random();
        double latitude = 38 + (40 - 38) * rand.nextDouble();
        double longitude = 28 + (43 - 28) * rand.nextDouble();
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

    public static String generateString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=[]{}|;:,.<>?/";
        Random random = new Random();
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            builder.append(randomChar);
        }

        return builder.toString();
    }
}

