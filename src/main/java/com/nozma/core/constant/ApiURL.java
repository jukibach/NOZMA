package com.nozma.core.constant;

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
    public static final String GET_EXERCISE_GUEST = "/v1/exercises/guest";
    public static final String ACTUATOR_HEALTH = "/actuator/health";
    public static final String ACTUATOR_INFO = "/actuator/info";
    public static final String UPDATE_DISPLAY_EXERCISE_SETTING_BY_ID = "/v1/exercises/display-setting/{id}";
}
