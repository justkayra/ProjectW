package com.semantyca.projectw.dto.constant;

public enum EmphasisType {
    UNKNOWN( "unknown"),SOLEMN( "solemn"), STRONGER("strong" ), WEAKER("low" ), OFFENSIVE("offensive" ), RANDOMLY("random"), OPPOSITE("opposite");

    private String alias;

    EmphasisType(String alias) {
        this.alias = alias;
    }



}
