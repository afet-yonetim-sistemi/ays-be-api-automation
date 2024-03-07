package org.ays.payload;

import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;
import org.ays.endpoints.InstitutionEndpoints;
import org.ays.utility.AysRandomUtil;

@Getter
@Setter
public class Assignment {

    private String description;
    private String firstName;
    private String lastName;
    private PhoneNumber phoneNumber;
    private Double latitude;
    private Double longitude;

    public static Assignment generateCreateAssignment() {
        Assignment assignment = generateCreateAssignmentPayload();
        Response response = InstitutionEndpoints.createAnAssignment(assignment);
        if (response.getStatusCode() == 200) {
            return assignment;
        } else {
            throw new RuntimeException("Assignment creation failed with status code: " + response.getStatusCode());
        }
    }

    public static Assignment generateCreateAssignmentPayload() {
        Assignment assignment = new Assignment();
        assignment.setFirstName(AysRandomUtil.generateFirstName());
        assignment.setLastName(AysRandomUtil.generateLastName());
        assignment.setPhoneNumber(PhoneNumber.generatePhoneNumber());
        assignment.setDescription(AysRandomUtil.generateDescription());
        assignment.setLatitude(AysRandomUtil.generateRandomCoordinate(38, 40));
        assignment.setLongitude(AysRandomUtil.generateRandomCoordinate(28, 43));
        return assignment;
    }

}
