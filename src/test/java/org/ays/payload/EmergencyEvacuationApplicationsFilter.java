package org.ays.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.utility.DatabaseUtility;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class EmergencyEvacuationApplicationsFilter {

    private String referenceNumber;
    private String sourceCity;
    private String sourceDistrict;
    private int seatingCount;
    private PhoneNumber phoneNumber;
    private String targetCity;
    private String targetDistrict;
    private List<String> statuses;
    private Boolean isInPerson;

    public static EmergencyEvacuationApplicationsFilter generate(EmergencyEvacuationApplication emergencyEvacuationApplication) {
        EmergencyEvacuationApplicationsFilter filter = new EmergencyEvacuationApplicationsFilter();
        filter.setReferenceNumber(DatabaseUtility.getLatestReferenceNumber());
        filter.setPhoneNumber(emergencyEvacuationApplication.getPhoneNumber());
        filter.setSourceCity(emergencyEvacuationApplication.getSourceCity());
        filter.setSourceDistrict(emergencyEvacuationApplication.getSourceDistrict());
        filter.setSeatingCount(emergencyEvacuationApplication.getSeatingCount());
        filter.setTargetCity(emergencyEvacuationApplication.getTargetCity());
        filter.setTargetDistrict(emergencyEvacuationApplication.getTargetDistrict());
        filter.setStatuses(Collections.singletonList("PENDING"));
        filter.setIsInPerson(true);
        return filter;
    }

}
