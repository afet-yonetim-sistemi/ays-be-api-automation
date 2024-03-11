package org.ays.payload;

import lombok.Getter;
import lombok.Setter;
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

    public static Assignment generate() {
        Assignment assignment = new Assignment();
        assignment.setFirstName(AysRandomUtil.generateFirstName());
        assignment.setLastName(AysRandomUtil.generateLastName());
        assignment.setPhoneNumber(PhoneNumber.generateForTurkey());
        assignment.setDescription(AysRandomUtil.generateDescription());
        assignment.setLatitude(AysRandomUtil.generateRandomCoordinate(38, 40));
        assignment.setLongitude(AysRandomUtil.generateRandomCoordinate(28, 43));
        return assignment;
    }

}
