package org.wallet.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.wallet.model.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(WalletNotFoundException.class)
    void walletNotFound(HttpServletResponse response, WalletNotFoundException e) throws IOException {
        sendResponse(response, HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(NotEnoughFundsException.class)
    void notEnoughMoney(HttpServletResponse response, NotEnoughFundsException e) throws IOException {
        sendResponse(response, HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    void badArgs(HttpServletResponse response, MethodArgumentNotValidException e) throws IOException {
        sendResponse(response, HttpStatus.BAD_REQUEST, new ModelException(e.getMessage(), ErrorType.BAD_INPUT));
    }

    @ExceptionHandler(PSQLException.class)
    void psqlException(HttpServletResponse response, PSQLException e) throws IOException {
        if(e.getSQLState().equals("40001")) {
            sendResponse(response,
                    HttpStatus.CONFLICT,
                    new ModelException(e.getMessage(), ErrorType.TRANSACTION_CONCURRENCY_CONFLICT));
            return;
        }
        throw new RuntimeException(e);
    }

    private void sendResponse(HttpServletResponse response, HttpStatus httpStatus, ModelException e) throws IOException {
        response.setStatus(httpStatus.value());
        ObjectMapper mapper = new ObjectMapper();
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()))) {
            writer.write(mapper.writeValueAsString(new ErrorResponse(e.getMessage(), e.getErrorType())));
        }


    }


}
