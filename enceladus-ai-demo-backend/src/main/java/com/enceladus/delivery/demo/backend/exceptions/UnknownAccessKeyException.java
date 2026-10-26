package com.enceladus.delivery.demo.backend.exceptions;

public class UnknownAccessKeyException extends Exception implements DashboardException {
    public UnknownAccessKeyException(String message) {
        super(message);
    }
}
