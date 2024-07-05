package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListEmergencyEvacuationApplications {
    private Pageable pageable;
    private EmergencyEvacuationApplicationsFilter filter;

    public static ListEmergencyEvacuationApplications generate(EmergencyEvacuationApplication emergencyEvacuationApplication) {
        ListEmergencyEvacuationApplications listEmergencyEvacuationApplications = new ListEmergencyEvacuationApplications();
        listEmergencyEvacuationApplications.setPageable(Pageable.generate(1, 10));
        listEmergencyEvacuationApplications.setFilter(EmergencyEvacuationApplicationsFilter.generate(emergencyEvacuationApplication));
        return listEmergencyEvacuationApplications;
    }
}
