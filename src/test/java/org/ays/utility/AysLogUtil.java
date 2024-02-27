package org.ays.utility;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.testng.Reporter;

@UtilityClass
@Slf4j
public class AysLogUtil {
    public static void customLog(String message) {
        log.info(message);
        Reporter.log(message);
    }
}
