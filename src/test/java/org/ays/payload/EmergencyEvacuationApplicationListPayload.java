package org.ays.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.common.model.payload.AysPageable;

@Getter
@Setter
public class EmergencyEvacuationApplicationListPayload {
    private AysPageable pageable;
    private EmergencyEvacuationApplicationsFilter filter;

    public static EmergencyEvacuationApplicationListPayload generate(EmergencyEvacuationApplication emergencyEvacuationApplication) {
        EmergencyEvacuationApplicationListPayload emergencyEvacuationApplicationListPayload = new EmergencyEvacuationApplicationListPayload();
        emergencyEvacuationApplicationListPayload.setPageable(AysPageable.generate(1, 10));
        emergencyEvacuationApplicationListPayload.setFilter(EmergencyEvacuationApplicationsFilter.generate(emergencyEvacuationApplication));
        return emergencyEvacuationApplicationListPayload;
    }
}
