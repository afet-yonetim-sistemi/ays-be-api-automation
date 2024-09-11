package org.ays.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.common.model.payload.AysPageable;

@Getter
@Setter
public class ListEmergencyEvacuationApplications {
    private AysPageable pageable;
    private EmergencyEvacuationApplicationsFilter filter;

    public static ListEmergencyEvacuationApplications generate(EmergencyEvacuationApplication emergencyEvacuationApplication) {
        ListEmergencyEvacuationApplications listEmergencyEvacuationApplications = new ListEmergencyEvacuationApplications();
        listEmergencyEvacuationApplications.setPageable(AysPageable.generate(1, 10));
        listEmergencyEvacuationApplications.setFilter(EmergencyEvacuationApplicationsFilter.generate(emergencyEvacuationApplication));
        return listEmergencyEvacuationApplications;
    }
}
