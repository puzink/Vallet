package org.wallet.model;

public class NotEnoughFundsException extends ModelException {

    public NotEnoughFundsException(String message) {
        super(message, ErrorType.NOT_ENOUGH_FUNDS);
    }

}
