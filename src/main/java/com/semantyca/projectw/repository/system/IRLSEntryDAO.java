package com.semantyca.projectw.repository.system;


import com.semantyca.projectw.model.embedded.RLSEntry;

import java.util.List;
import java.util.UUID;


public interface IRLSEntryDAO{


    List<RLSEntry> findByDocumentId(UUID enityId);

}



