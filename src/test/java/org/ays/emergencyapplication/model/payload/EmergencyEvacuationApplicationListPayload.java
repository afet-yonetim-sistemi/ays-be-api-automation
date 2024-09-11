package org.ays.emergencyapplication.model.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.common.model.payload.AysPageable;
import org.ays.payload.PhoneNumber;
import org.ays.utility.DatabaseUtility;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class EmergencyEvacuationApplicationListPayload {

    private AysPageable pageable;
    private EmergencyEvacuationApplicationsFilter filter;

    public static EmergencyEvacuationApplicationListPayload generate(EmergencyEvacuationApplicationPayload emergencyEvacuationApplicationPayload) {
        EmergencyEvacuationApplicationListPayload emergencyEvacuationApplicationListPayload = new EmergencyEvacuationApplicationListPayload();
        emergencyEvacuationApplicationListPayload.setPageable(AysPageable.generate(1, 10));
        emergencyEvacuationApplicationListPayload.setFilter(EmergencyEvacuationApplicationsFilter.generate(emergencyEvacuationApplicationPayload));
        return emergencyEvacuationApplicationListPayload;
    }

    @Getter
    @Setter
    public static class EmergencyEvacuationApplicationsFilter {

        private String referenceNumber;
        private String sourceCity;
        private String sourceDistrict;
        private int seatingCount;
        private PhoneNumber phoneNumber;
        private String targetCity;
        private String targetDistrict;
        private List<String> statuses;
        private Boolean isInPerson;

        public static EmergencyEvacuationApplicationsFilter generate(EmergencyEvacuationApplicationPayload emergencyEvacuationApplicationPayload) {
            EmergencyEvacuationApplicationsFilter filter = new EmergencyEvacuationApplicationsFilter();
            filter.setReferenceNumber(DatabaseUtility.getLatestReferenceNumber());
            filter.setPhoneNumber(emergencyEvacuationApplicationPayload.getPhoneNumber());
            filter.setSourceCity(emergencyEvacuationApplicationPayload.getSourceCity());
            filter.setSourceDistrict(emergencyEvacuationApplicationPayload.getSourceDistrict());
            filter.setSeatingCount(emergencyEvacuationApplicationPayload.getSeatingCount());
            filter.setTargetCity(emergencyEvacuationApplicationPayload.getTargetCity());
            filter.setTargetDistrict(emergencyEvacuationApplicationPayload.getTargetDistrict());
            filter.setStatuses(Collections.singletonList("PENDING"));
            filter.setIsInPerson(true);
            return filter;
        }

    }


}
