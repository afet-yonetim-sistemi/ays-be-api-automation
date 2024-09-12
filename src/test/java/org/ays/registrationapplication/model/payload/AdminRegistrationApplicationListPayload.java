package org.ays.registrationapplication.model.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.common.model.payload.AysPageable;
import org.ays.registrationapplication.model.enums.AdminRegistrationApplicationStatus;

import java.util.List;

@Getter
@Setter
public class AdminRegistrationApplicationListPayload {

    private AysPageable pageable;
    private Filter filter;

    public static AdminRegistrationApplicationListPayload generate() {
        AdminRegistrationApplicationListPayload payload = new AdminRegistrationApplicationListPayload();
        payload.setPageable(AysPageable.generateFirstPage());
        payload.setFilter(new Filter());
        return payload;
    }

    public static AdminRegistrationApplicationListPayload generate(Filter filter) {
        AdminRegistrationApplicationListPayload payload = new AdminRegistrationApplicationListPayload();
        payload.setPageable(AysPageable.generateFirstPage());
        payload.setFilter(filter);
        return payload;
    }

    @Getter
    @Setter
    public static class Filter {

        private List<AdminRegistrationApplicationStatus> statuses;

        public static Filter generate(AdminRegistrationApplicationStatus status) {
            Filter filter = new Filter();
            filter.setStatuses(List.of(status));
            return filter;
        }

    }

}
