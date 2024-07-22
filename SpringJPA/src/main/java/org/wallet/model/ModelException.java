package org.wallet.model;

import lombok.Getter;

@Getter
public class ModelException extends RuntimeException {

    private final ErrorType errorType;

    public ModelException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

}
