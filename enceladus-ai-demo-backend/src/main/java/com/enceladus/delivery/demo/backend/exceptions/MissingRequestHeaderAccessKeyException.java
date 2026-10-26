package com.enceladus.delivery.demo.backend.exceptions;

public class MissingRequestHeaderAccessKeyException extends Exception implements DashboardException {
    MissingRequestHeaderAccessKeyException(String message) {
        super(message);
    }
}
