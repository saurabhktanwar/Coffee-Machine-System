package com.dunzo.assignment.CoffeeMachine.exception;

public enum ErrorCode {

    INVALID_OUTLET(1001, "The selected outlet is invalid, and does not exist in the machine"),
    BEVERAGE_NOT_SUPPORTED(1002, "The requested beverage is not supported by the machine currently"),
    OUTLET_CURRENTLY_UNAVAILABLE(1003,
            "The selected outlet is currently being used, and is unavailable. please, try after some time."),
    REFILL_QUANTITY_SHOULD_NOT_BE_NEGATIVE(1004, "Refill quantity should not be a negative value.");

    private int    errorCode;
    private String message;

    ErrorCode(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getMessage() {
        return this.message;
    }

    public String getFormattedErrorMessage() {
        return String.format("error code: %s, message: %s", this.errorCode, this.message);
    }
}
