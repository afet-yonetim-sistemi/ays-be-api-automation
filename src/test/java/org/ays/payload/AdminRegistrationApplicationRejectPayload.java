package org.ays.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.utility.AysRandomUtil;

@Getter
@Setter
public class AdminRegistrationApplicationRejectPayload {

    private String rejectReason;

    public static AdminRegistrationApplicationRejectPayload generate() {
        AdminRegistrationApplicationRejectPayload reason = new AdminRegistrationApplicationRejectPayload();
        reason.setRejectReason(AysRandomUtil.generateRejectionReason());
        return reason;
    }

}