package com.semantyca.projectw.service;

import com.semantyca.projectw.client.DatamuseConnection;
import com.semantyca.projectw.dto.DatamuseWordDTO;
import com.semantyca.projectw.dto.constant.WordType;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class DatamuseService {
    private static final Logger LOGGER = LoggerFactory.getLogger("DatamuseService");

    @RestClient
    DatamuseConnection datamuseConnection;


    public List<DatamuseWordDTO> getSynonyms(String word) {
        LOGGER.info("fetch word  \"" + word + "\"");
        return datamuseConnection.get(word);
    }

    public WordType getWordType(String wordValue)  {
        LOGGER.info("check word  \"" + wordValue + "\"");
        List<DatamuseWordDTO> datamuseWordDTOList = datamuseConnection.getWordType(wordValue, "p");
        if (datamuseWordDTOList.size() > 0) {
            List<String> tags = datamuseWordDTOList.get(0).getTags();
            if (tags != null) {
                return WordType.getType(tags.get(0).toLowerCase());
            }
        }
        return WordType.UNKNOWN;
    }
}
