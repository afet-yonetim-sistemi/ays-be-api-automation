package org.ays.utility;

public enum ErrorMessage {
    MUST_NOT_BE_BLANK("must not be blank"),
    NAME_LENGTH("name must be between 2 and 100 characters long"),
    MUST_BE_VALID("must be valid"),
    SIZE_BETWEEN("size must be between 20 and 250"),
    SEATING_COUNT_BETWEEN("must be between 1 and 999"),
    ALL_APPLICANT_FIELDS_FILLED("all applicant fields must be filled");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
