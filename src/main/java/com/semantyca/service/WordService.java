package com.semantyca.service;

import com.semantyca.dto.WordDTO;
import com.semantyca.dto.constant.WordType;
import com.semantyca.model.Word;
import com.semantyca.repository.WordRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class WordService {
    private static final Logger LOGGER = LoggerFactory.getLogger("WordService");

    WordRepository wordRepository;

    @Inject
    public WordService(Jdbi jdbi, PgPool dbClient) {
        this.wordRepository = new WordRepository(jdbi, dbClient);
    }

    public Multi<Word> getAll() {
        return wordRepository.findAllUnrestricted(100, 0);
    }

    public Optional<Word> get(String word) {
        return wordRepository.findByValue(word);
    }

    public Uni<UUID> save(WordDTO wordDTO) {
        return save(wordDTO.getValue(), WordType.getType(wordDTO.getType()));
    }

    public Uni<UUID> save(String value, WordType type) {
        Word word = new Word.Builder()
                .setValue(value)
                .setType(type)
                .build();
        return wordRepository.save(word);
    }




}
