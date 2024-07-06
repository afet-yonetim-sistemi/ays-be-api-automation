package org.ays.utility;

public enum ErrorMessage {
    MUST_NOT_BE_BLANK("must not be blank"),
    MUST_BE_VALID("must be valid"),
    SIZE_BETWEEN("size must be between 20 and 250"),
    SEATING_COUNT_BETWEEN("must be between 1 and 999"),
    ALL_APPLICANT_FIELDS_FILLED("all applicant fields must be filled"),
    SIZE_BETWEEN_2_100("size must be between 2 and 100"),
    CONTAINS_INVALID_CHARACTERS("contains invalid characters"),
    MUST_BE_ACCEPTED_VALUE("must be accepted value"),
    MUST_NOT_BE_NULL("must not be null"),
    MUST_BE_TRUE("must be true");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
