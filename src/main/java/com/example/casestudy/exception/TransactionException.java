package com.example.casestudy.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TransactionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TransactionException(String message) {
        super(message);
    }
}