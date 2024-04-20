package org.ays.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.utility.AysRandomUtil;

@Getter
@Setter
public class RejectReason {

    private String rejectReason;

    public static RejectReason generate() {
        RejectReason reason = new RejectReason();
        reason.setRejectReason(AysRandomUtil.generateRejectionReason());
        return reason;
    }

}
