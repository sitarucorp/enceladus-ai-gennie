package com.enceladus.delivery.demo.backend.handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.util.stream.Collectors;

@ConditionalOnProperty(name = "dashboard.trace.http", havingValue = "true")
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        logger.info("Request Method: {}, Request URL: {}", request.getMethod(), request.getRequestURL());
        logger.info("Request Body: {}", request.getReader().lines().collect(Collectors.joining(System.lineSeparator())));

        CustomHttpServletResponseWrapper responseWrapper = new CustomHttpServletResponseWrapper(response);
        request.setAttribute("responseWrapper", responseWrapper);

        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, HttpServletResponse response, @NonNull Object handler, Exception ex) throws Exception {
        logger.info("Response Status: {}", response.getStatus());
        CustomHttpServletResponseWrapper wrapper = new CustomHttpServletResponseWrapper(response);
        logger.info("Response Body => {}", wrapper.getCaptureAsString());
    }
}

class CustomHttpServletResponseWrapper extends HttpServletResponseWrapper {

    private final CharArrayWriter charArrayWriter = new CharArrayWriter();
    private final PrintWriter writer = new PrintWriter(charArrayWriter);

    public CustomHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public PrintWriter getWriter() {
        return writer;
    }

    public String getCaptureAsString() {
        return charArrayWriter.toString();
    }
}