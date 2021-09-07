package com.semantyca.service;

import com.semantyca.client.DatamuseConnection;
import com.semantyca.dto.DatamuseWordDTO;
import com.semantyca.dto.constant.WordType;
import com.semantyca.repository.AdjectiveRepository;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.List;

@ApplicationScoped
public class DatamuseService {
    private static final Logger LOGGER = LoggerFactory.getLogger("DatamuseService");
    private static final HashMap<String, WordType> cache = new HashMap();

    @RestClient
    DatamuseConnection datamuseConnection;

    private AdjectiveRepository adjectiveRepository;


    public DatamuseService(Jdbi jdbi) {
        adjectiveRepository = new AdjectiveRepository(jdbi);
    }
    public List<DatamuseWordDTO> getWord(String word) {
        LOGGER.info("fetch word  \"" + word + "\"");
        return datamuseConnection.get(word);
    }

    public WordType getWordType(String word) {
       word = word.replace(",", "");
       LOGGER.info("check word  \"" + word + "\"");
       WordType wordType = cache.get(word);
       if (wordType == null) {
           List<DatamuseWordDTO> datamuseWordDTOList = datamuseConnection.getWordType(word, "p");
           if (datamuseWordDTOList.size() > 0) {
               DatamuseWordDTO datamuseWordDTO = datamuseWordDTOList.get(0);
               List<String> tags = datamuseWordDTO.getTags();
               if (tags != null) {
                   WordType newWordType = WordType.getType(datamuseWordDTO.getTags().get(0).toLowerCase());
                   cache.put(word, newWordType);
                   return newWordType;
               } else {
                   cache.put(word, WordType.UNKNOWN);
                   return WordType.UNKNOWN;
               }
           } else {
               cache.put(word, WordType.UNKNOWN);
               return WordType.UNKNOWN;
           }
       } else {
           LOGGER.info("word was taken from cache");
           return wordType;
       }
    }
}
