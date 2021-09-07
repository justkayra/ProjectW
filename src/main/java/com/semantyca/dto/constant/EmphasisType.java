package com.semantyca.dto.constant;

import com.semantyca.localization.LanguageCode;

public enum EmphasisType {
    UNKNOWN( "unknown"),SOLEMN( "solemn"), STRONG("strong" ), LOW("low" ), OFFENSIVE("offensive" ), RANDOM("random");

    private String alias;

    EmphasisType(String alias) {
        this.alias = alias;
    }



}
