package com.dunzo.assignment.CoffeeMachine.exception;

public class CoffeeMachineException extends Exception {

    private ErrorCode errorCode;

    public CoffeeMachineException() {
        super();
    }

    public CoffeeMachineException(ErrorCode errorCode) {
        super(errorCode.getFormattedErrorMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }
}
