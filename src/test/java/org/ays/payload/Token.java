package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Token {

    private String accessToken;
    private String refreshToken;
    private String accessTokenExpiresAt;

}
