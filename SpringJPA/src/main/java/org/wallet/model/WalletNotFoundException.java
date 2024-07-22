package org.wallet.model;

public class WalletNotFoundException extends ModelException {

    private static final ErrorType TYPE = ErrorType.WALLET_NOT_FOUND;

    public WalletNotFoundException(String message) {
        super(message, TYPE);
    }
}
