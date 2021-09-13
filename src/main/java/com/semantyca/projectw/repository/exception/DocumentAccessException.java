package com.semantyca.projectw.repository.exception;

import java.util.UUID;


public class DocumentAccessException extends Exception {
    private UUID docId;
    private int absentRight;

    public DocumentAccessException(UUID id, int editIsAllowed) {
        super();
        docId = id;
        absentRight = editIsAllowed;
    }

    public String getDeveloperMessage() {
        return "\"" + docId.toString() + "\" restricted to modify, lack of " + absentRight;
    }
}
