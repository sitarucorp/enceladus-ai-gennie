package com.enceladus.delivery.demo.commons.model.core;

public interface Constants {

    public static final String REQUEST_ID_HEADER_NAME = "x-request-id";
    public static final String API_KEY_TENANT_NAME = "x-tenant";
    public static final String API_KEY_HEADER_NAME = "x-api-key";
    public static final String DASHBOARD_API_KEY = "Z2lyaXNoLm1haGFqYW5AbXBoYXNpcy5jb20K";

    /**
     * Default page size to be respond in get call if explicit page size is not provided
    * */
    public static final String DEFAULT_PAGE_SIZE = "10";


    /**
     * Default error codes when advisor responds on any exception caught
     *
     */
    public enum API_ERRORS {
        MISSING_ACCESS_KEY_HEADER, INVALID_API_KEY, ERROR
    }
}
