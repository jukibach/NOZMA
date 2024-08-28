package com.ecommerce.userservice.constant;

public class ApiURL {
    private ApiURL() {
    }
    public static final String ROOT_PATH = "/api";
    public static final String GET_ACCOUNTS = "/v1/accounts/listAccount";
    public static final String ACCOUNT_BY_ID = "/v1/accounts/{id}";
    public static final String LOGIN = "/auth/login";
    public static final String REGISTER_USER = "/auth/register/users";
    public static final String REISSUE_TOKEN = "/auth/reissue-token";
    public static final String CHANGE_PASSWORD = "/auth/change-password";
    public static final String REISSUE_PASSWORD = "/auth/reissue-password";
    public static final String LOGOUT = "/auth/logout";
    
    public static final String GET_EXERCISE = "/v1/exercises";
    
}
