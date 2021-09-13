package com.semantyca.projectw.dto.constant;

public enum ResultType {
    UNKNOWN(0, "unknown"),SUCCESS(100, "success"), NOT_SUCCESS(101, "not successful" );

    private int code;
    private String alias;

    ResultType(int code, String lang) {
        this.code = code;
        this.alias = lang;
    }

}
