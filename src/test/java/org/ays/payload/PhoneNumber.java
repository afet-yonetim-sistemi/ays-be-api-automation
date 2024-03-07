package org.ays.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.utility.AysRandomUtil;

@Getter
@Setter
public class PhoneNumber {

    private String countryCode;
    private String lineNumber;

    public static PhoneNumber generatePhoneNumber() {
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setLineNumber(AysRandomUtil.generateLineNumber());
        phoneNumber.setCountryCode("90");
        return phoneNumber;
    }

}
