package org.ays.auth.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRefreshPayload {

    private String refreshToken;

}
