package com.semantyca.dto.constant;

public enum WordType {
    UNKNOWN("null"), NOUN("n"), ADJECTIVE("adj"), ADVERB("adv"), VERB("v");

    private String alias;

    WordType(String alias) {
        this.alias = alias;
    }

    public static WordType getType(String a) {
        for (WordType type : values()) {
            if (type.alias.equals(a)) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
