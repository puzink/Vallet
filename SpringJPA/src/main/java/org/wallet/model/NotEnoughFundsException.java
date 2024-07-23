package org.wallet.model;

public class NotEnoughFundsException extends ModelException {

    private static final ErrorType ERROR = ErrorType.NOT_ENOUGH_FUNDS;

    public NotEnoughFundsException(String message) {
        super(message, ERROR);
    }

    public NotEnoughFundsException(String message, Throwable cause) {
        super(message, cause, ERROR);
    }

}
