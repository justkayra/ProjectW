package com.semantyca.projectw.dto.constant;

public enum WordType {
    UNKNOWN(0, "null"),
    ARTICLE(100,"a"),
    NOUN(101,"n"),
    ADJECTIVE(102,"adj"),
    ADVERB(103,"adv"),
    VERB(104,"v"),
    SYSTEM(110, "s");

    private int code;
    private String alias;

    WordType(int code, String alias) {
        this.code = code;
        this.alias = alias;
    }

    public static WordType getType(int code) {
        for (WordType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        return UNKNOWN;
    }

    public int getCode() {
        return code;
    }


    public static WordType getType(String a) {
        for (WordType type : values()) {
            if (type.alias.equals(a)) {
                return type;
            }
        }
        return UNKNOWN;
    }

    public String getAlias() {
        return alias;
    }
}
