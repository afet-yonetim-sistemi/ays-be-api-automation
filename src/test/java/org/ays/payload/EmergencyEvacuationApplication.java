package org.ays.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.utility.AysRandomUtil;

@Getter
@Setter
public class EmergencyEvacuationApplication {
    private String applicantFirstName;
    private String applicantLastName;
    private PhoneNumber applicantPhoneNumber;
    private String firstName;
    private String lastName;
    private PhoneNumber phoneNumber;
    private String sourceCity;
    private String sourceDistrict;
    private String address;
    private int seatingCount;
    private String targetCity;
    private String targetDistrict;


    public static EmergencyEvacuationApplication generateForMe() {
        return generate(false);
    }

    public static EmergencyEvacuationApplication generateForOtherPerson() {
        return generate(true);
    }

    private static EmergencyEvacuationApplication generate(boolean forOtherPerson) {
        EmergencyEvacuationApplication application = new EmergencyEvacuationApplication();

        if (forOtherPerson) {
            application.setApplicantFirstName(AysRandomUtil.generateFirstName());
            application.setApplicantLastName(AysRandomUtil.generateLastName());
            application.setApplicantPhoneNumber(PhoneNumber.generateForTurkey());
        }

        application.setFirstName(AysRandomUtil.generateFirstName());
        application.setLastName(AysRandomUtil.generateLastName());
        application.setPhoneNumber(PhoneNumber.generateForTurkey());
        application.setSourceCity(AysRandomUtil.generateRandomCity());
        application.setSourceDistrict(AysRandomUtil.generateRandomDistrict());
        application.setAddress(AysRandomUtil.generateRandomAddress());
        application.setSeatingCount(AysRandomUtil.generateSeatingCount());
        application.setTargetCity(AysRandomUtil.generateRandomCity());
        application.setTargetDistrict(AysRandomUtil.generateRandomDistrict());

        return application;
    }
}
