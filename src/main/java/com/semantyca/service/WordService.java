package com.semantyca.service;

import com.semantyca.dto.FeedbackEntry;
import com.semantyca.dto.ProcessFeedback;
import com.semantyca.dto.WordDTO;
import com.semantyca.dto.constant.MessageLevel;
import com.semantyca.dto.constant.RatingType;
import com.semantyca.dto.constant.WordType;
import com.semantyca.model.Word;
import com.semantyca.repository.WordRepository;
import com.semantyca.repository.exception.DocumentExists;
import io.vertx.mutiny.pgclient.PgPool;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class WordService {
    private static final Logger LOGGER = LoggerFactory.getLogger("WordService");

    WordRepository wordRepository;

    @Inject
    public WordService(Jdbi jdbi, PgPool dbClient) {
        this.wordRepository = new WordRepository(jdbi);
    }

    public List<Word> getAll() {
        return wordRepository.findAllUnrestricted(100, 0);
    }

    public Optional<Word> getById(String id) {
        return wordRepository.findById(UUID.fromString(id));
    }

    public Optional<Word> getByWord(String word) {
        return wordRepository.findByValue(word);
    }

    public List<Word> getAllAssociations() {
        return wordRepository.findAllUnrestricted(100, 0);
    }

    public Word add(String word, WordType wordType) throws DocumentExists {
        WordDTO wordDTO = new WordDTO();
        wordDTO.setValue(word);
        wordDTO.setType(wordType.getAlias());
        return add(wordDTO);
    }

    public Word add(WordDTO dto) throws DocumentExists {
        Optional<Word> wordOptional = wordRepository.findByValue(dto.getValue());
        if (wordOptional.isEmpty()) {
            List<Word> relatedWords = new ArrayList<>();
            List<String> associationsList = dto.getAssociations();
            if (associationsList != null) {
                for (String a : dto.getAssociations()) {
                    Optional<Word> association = wordRepository.findByValue(a);
                    if (association.isPresent()) {
                        relatedWords.add(association.get());
                    } else {
                        Word associatedAdj = new Word.Builder()
                                .setValue(a)
                                .build();
                        Word associatedAdjective = wordRepository.insert(associatedAdj);
                        relatedWords.add(associatedAdjective);
                    }
                }
            }
            Word word = new Word.Builder()
                    .setValue(dto.getValue())
                    .setAssociations(relatedWords)
                    .setType(WordType.getType(dto.getType()))
                    .build();
            return wordRepository.insert(word);
        } else {
            throw new DocumentExists(dto.getValue());
        }
    }

    public int updateEmphasisRank(String id, String associatedWord, String rateAsText ) {
        Optional<Word> wordOptional = wordRepository.findById(UUID.fromString(id));
        return wordRepository.updateRates(wordOptional.get(), RatingType.EMPHASIS, associatedWord, Integer.parseInt(rateAsText));
    }

    public ProcessFeedback delete(String id) {
        ProcessFeedback feedback = new ProcessFeedback();
        if (id.equals("all")) {
            List<Word> wordList = wordRepository.findAllUnrestricted(0, 0);
            for (Word word : wordList) {
                feedback.addEntry(buildFeedBackEntry(id, wordRepository.bareDelete(word)));
            }
        } else {
            Optional<Word> wordOptional = wordRepository.findById(UUID.fromString(id));
            if (wordOptional.isPresent()) {
                feedback.addEntry(buildFeedBackEntry(id, wordRepository.bareDelete(wordOptional.get())));
            } else {
                FeedbackEntry feedbackEntry = new FeedbackEntry();
                feedbackEntry.setId(id);
                feedbackEntry.setLevel(MessageLevel.FAILURE);
                feedbackEntry.setDescription("Document not found");
                feedback.addEntry(feedbackEntry);
                LOGGER.debug("Document ${u} not found", id);

            }
        }
        return feedback;
    }

/*    public Uni<UUID> save(WordDTO wordDTO) {
        return save(wordDTO.getValue(), WordType.getType(wordDTO.getType()));
    }*/
/*

    public Uni<UUID> save(String value, WordType type) {
        Word word = new Word.Builder()
                .setValue(value)
                .setType(type)
                .build();
        return wordRepository.saveAsync(word);
    }
*/



    private FeedbackEntry buildFeedBackEntry(String id, int result) {
        FeedbackEntry feedbackEntry = new FeedbackEntry();
        feedbackEntry.setId(id);
        if (result == 1) {
            feedbackEntry.setLevel(MessageLevel.SUCCESS);
            feedbackEntry.setDescription("Document has been deleted");
            LOGGER.debug("Document ${u} has been deleted", id);
        } else {
            feedbackEntry.setLevel(MessageLevel.FAILURE);
            feedbackEntry.setDescription("Something wrong happened during deleting process");
            LOGGER.debug("Document ${u} didnt delete", id);
        }
        return feedbackEntry;
    }


}
