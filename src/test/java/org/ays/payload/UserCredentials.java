package org.ays.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.auth.model.enums.SourcePage;
import org.ays.utility.AysConfigurationProperty;

@Getter
@Setter
public class UserCredentials {
    private String emailAddress;
    private String password;
    private SourcePage sourcePage;

    public static UserCredentials generate() {
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setEmailAddress(AysConfigurationProperty.LandingUserOne.EMAIL_ADDRESS);
        userCredentials.setPassword(AysConfigurationProperty.LandingUserOne.PASSWORD);
        userCredentials.setSourcePage(SourcePage.LANDING);

        return userCredentials;
    }

}
