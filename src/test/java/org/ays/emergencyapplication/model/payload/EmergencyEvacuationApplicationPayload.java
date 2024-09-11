package org.ays.emergencyapplication.model.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.payload.AysPhoneNumber;
import org.ays.utility.AysRandomUtil;

@Getter
@Setter
public class EmergencyEvacuationApplicationPayload {

    private String applicantFirstName;
    private String applicantLastName;
    private AysPhoneNumber applicantPhoneNumber;
    private String firstName;
    private String lastName;
    private AysPhoneNumber phoneNumber;
    private String sourceCity;
    private String sourceDistrict;
    private String address;
    private int seatingCount;
    private String targetCity;
    private String targetDistrict;


    public static EmergencyEvacuationApplicationPayload generateForMe() {
        return generate(false);
    }

    public static EmergencyEvacuationApplicationPayload generateForOtherPerson() {
        return generate(true);
    }

    private static EmergencyEvacuationApplicationPayload generate(boolean forOtherPerson) {
        EmergencyEvacuationApplicationPayload application = new EmergencyEvacuationApplicationPayload();

        if (forOtherPerson) {
            application.setApplicantFirstName(AysRandomUtil.generateFirstName());
            application.setApplicantLastName(AysRandomUtil.generateLastName());
            application.setApplicantPhoneNumber(AysPhoneNumber.generateForTurkey());
        }

        application.setFirstName(AysRandomUtil.generateFirstName());
        application.setLastName(AysRandomUtil.generateLastName());
        application.setPhoneNumber(AysPhoneNumber.generateForTurkey());
        application.setSourceCity(AysRandomUtil.generateRandomCity());
        application.setSourceDistrict(AysRandomUtil.generateRandomDistrict());
        application.setAddress(AysRandomUtil.generateRandomAddress());
        application.setSeatingCount(AysRandomUtil.generateSeatingCount());
        application.setTargetCity(AysRandomUtil.generateRandomCity());
        application.setTargetDistrict(AysRandomUtil.generateRandomDistrict());

        return application;
    }
}
