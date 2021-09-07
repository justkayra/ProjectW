package com.semantyca.repository.exception;

import java.util.UUID;


public class DocumentNotFoundException extends Exception {
    private UUID docId;

    public DocumentNotFoundException(UUID id) {
        super();
        docId = id;
    }

    public String getDeveloperMessage() {
        return docId.toString() + " not found";
    }
}
