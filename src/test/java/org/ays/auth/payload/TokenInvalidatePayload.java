package org.ays.auth.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenInvalidatePayload {

    private String accessToken;
    private String refreshToken;

}
