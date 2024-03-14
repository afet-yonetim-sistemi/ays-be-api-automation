package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSupportStatusUpdatePayload {

    private UserSupportStatus supportStatus;

    public UserSupportStatusUpdatePayload(UserSupportStatus supportStatus) {
        this.supportStatus = supportStatus;
    }

}
