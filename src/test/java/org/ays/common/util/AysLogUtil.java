package org.ays.common.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.testng.Reporter;

@UtilityClass
@Slf4j
public class AysLogUtil {
    public static void info(String message) {
        log.info(message);
        Reporter.log(message);
    }
}
