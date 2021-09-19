package com.semantyca.projectw.dto.constant;

public enum EmphasisType {
    UNKNOWN( "unknown"),SOLEMN( "solemn"), STRONG("strong" ), LOW("low" ), OFFENSIVE("offensive" ), RANDOM("random"), OPPOSITE("opposite");

    private String alias;

    EmphasisType(String alias) {
        this.alias = alias;
    }



}
