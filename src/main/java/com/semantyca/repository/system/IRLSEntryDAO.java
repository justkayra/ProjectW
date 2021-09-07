package com.semantyca.repository.system;


import com.semantyca.model.embedded.RLSEntry;

import java.util.List;
import java.util.UUID;


public interface IRLSEntryDAO{


    List<RLSEntry> findByDocumentId(UUID enityId);

}



