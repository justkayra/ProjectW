package com.semantyca.service;

import com.semantyca.client.DatamuseConnection;
import com.semantyca.dto.DatamuseWordDTO;
import com.semantyca.dto.constant.WordType;
import com.semantyca.model.Word;
import com.semantyca.repository.AdjectiveRepository;
import com.semantyca.repository.WordRepository;
import com.semantyca.repository.exception.DocumentExists;
import io.vertx.mutiny.pgclient.PgPool;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class DatamuseService {
    private static final Logger LOGGER = LoggerFactory.getLogger("DatamuseService");
    private static final HashMap<String, WordType> cache;

    @RestClient
    DatamuseConnection datamuseConnection;

    private AdjectiveRepository adjectiveRepository;

    private WordRepository wordRepository;

    private WordService wordService;

    @Inject
    public DatamuseService(Jdbi jdbi, PgPool dbClient, WordService wordService) {
        adjectiveRepository = new AdjectiveRepository(jdbi);
        wordRepository = new WordRepository(jdbi);
        this.wordService = wordService;

    }
    public List<DatamuseWordDTO> getWord(String word) {
        LOGGER.info("fetch word  \"" + word + "\"");
        return datamuseConnection.get(word);
    }

    public WordType getWordType(String word) throws DocumentExists {
       WordType wordType = cache.get(word);
       if (wordType == null) {
           LOGGER.info("request for:  \"" + word + "\"");
           Optional<Word> wordOptional = wordService.getByWord(word);
           if (wordOptional.isPresent()){
                wordType = wordOptional.get().getType();
               cache.put(word, wordType);
           } else {
               List<DatamuseWordDTO> datamuseWordDTOList = datamuseConnection.getWordType(word, "p");
               if (datamuseWordDTOList.size() > 0) {
                   List<String> tags = datamuseWordDTOList.get(0).getTags();
                   if (tags != null) {
                       wordType = WordType.getType(tags.get(0).toLowerCase());
                       wordService.add(word, wordType);
                       cache.put(word, wordType);
                       return wordType;
                   } else {
                       cache.put(word, WordType.UNKNOWN);
                       return WordType.UNKNOWN;
                   }
               } else {
                   cache.put(word, WordType.UNKNOWN);
                   return WordType.UNKNOWN;
               }
           }
       }
        return wordType;
    }

    static {
        cache = new HashMap<>();
        cache.put("&#32;", WordType.SYSTEM);
        cache.put("&#44;", WordType.SYSTEM);
        cache.put("&#46;", WordType.SYSTEM);
        cache.put("&#34;", WordType.SYSTEM);
    }
}
