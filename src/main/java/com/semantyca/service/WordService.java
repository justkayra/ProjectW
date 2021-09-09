package com.semantyca.service;

import com.semantyca.dto.WordDTO;
import com.semantyca.dto.constant.WordType;
import com.semantyca.model.Word;
import com.semantyca.repository.AdjectiveRepository;
import com.semantyca.repository.WordRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class WordService {
    private static final Logger LOGGER = LoggerFactory.getLogger("WordService");
    private AdjectiveRepository adjectiveRepository;

    @Inject
    private PgPool dbClient;

    @Inject
    public WordService(PgPool dbClient) {
        this.dbClient = dbClient;
    }

    public Multi<Word> get() {
        return WordRepository.findAllUnrestricted(dbClient, 100, 0);
    }

    public Uni<Word> get(String id) {
        return WordRepository.findById(dbClient, 100, 0);
    }

    public Uni<UUID> save(WordDTO wordDTO) {
        Word word = new Word.Builder()
                .setValue(wordDTO.getValue())
                .setType(WordType.getType(wordDTO.getType()))
                .build();
        return WordRepository.save(dbClient, word);
    }


}
