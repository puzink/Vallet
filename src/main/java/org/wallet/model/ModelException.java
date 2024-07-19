package org.wallet.model;

import lombok.Getter;

@Getter
public class ModelException extends RuntimeException {

    private final ErrorType errorType;

    public ModelException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public ModelException(Throwable cause, ErrorType errorType) {
        super(cause);
        this.errorType = errorType;
    }

}
