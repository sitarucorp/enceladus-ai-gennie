package com.enceladus.delivery.demo.backend.exceptions;

import com.enceladus.delivery.demo.commons.model.core.Constants;
import com.enceladus.delivery.demo.commons.model.core.DashboardResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DashboardBackendExceptionAdvisor {

    @ExceptionHandler(exception = MissingRequestHeaderAccessKeyException.class)
    public ResponseEntity<DashboardResponse<Object>> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        return ResponseEntity.ok().body(DashboardResponse.builder().data("")
                .status(Constants.API_ERRORS.MISSING_ACCESS_KEY_HEADER.name())
                .message("Dashboard Error : Missing x-api-key header").build());
    }

    @ExceptionHandler(exception = UnknownAccessKeyException.class)
    public ResponseEntity<DashboardResponse<Object>> handleUnknownAccessKeyException(UnknownAccessKeyException ex) {
        return ResponseEntity.ok().body(DashboardResponse.builder().data("")
                .status(Constants.API_ERRORS.INVALID_API_KEY.name())
                .message("Dashboard Error : Invalid x-api-key header").build());
    }

    @ExceptionHandler(exception = Exception.class)
    public ResponseEntity<DashboardResponse<Object>> handleException(Exception ex) {
        return ResponseEntity.ok().body(DashboardResponse.builder().data("")
                .status(Constants.API_ERRORS.ERROR.name())
                .message("Backend Error : " + ex.getMessage()).build());
    }
}
