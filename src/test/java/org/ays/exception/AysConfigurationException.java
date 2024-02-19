package org.ays.exception;

import java.io.Serial;

public class AysConfigurationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -3108411992530107699L;

    public AysConfigurationException(String message) {
        super(message);
    }

    public AysConfigurationException(Throwable cause) {
        super(cause);
    }

}
