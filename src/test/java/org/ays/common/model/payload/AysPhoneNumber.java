package org.ays.common.model.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.utility.AysRandomUtil;

@Getter
@Setter
public class AysPhoneNumber {

    private String countryCode;
    private String lineNumber;

    public static AysPhoneNumber generateForTurkey() {
        AysPhoneNumber phoneNumber = new AysPhoneNumber();
        phoneNumber.setLineNumber(AysRandomUtil.generateLineNumber());
        phoneNumber.setCountryCode("90");
        return phoneNumber;
    }

}
