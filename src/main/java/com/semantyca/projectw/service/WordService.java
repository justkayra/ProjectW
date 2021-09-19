package com.semantyca.projectw.service;

import com.semantyca.projectw.dto.*;
import com.semantyca.projectw.dto.constant.MessageLevel;
import com.semantyca.projectw.dto.constant.RatingType;
import com.semantyca.projectw.dto.constant.WordType;
import com.semantyca.projectw.model.Word;
import com.semantyca.projectw.repository.WordRepository;
import com.semantyca.projectw.repository.exception.DocumentExists;
import io.vertx.sqlclient.Tuple;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class WordService {
    private static final Logger LOGGER = LoggerFactory.getLogger("WordService");
    private static final HashMap<String, WordType> cache;
    private static final String[] sysWords = {"&#32;", "&#44;", "&#46;", "&#34;", "&#39;"};
    private static final boolean CHECK_SYNONYMS_FROM_EXT = true;
    private static final boolean CHECK_ANTONYMS_FROM_EXT = true;

    WordRepository repository;

    private DatamuseService datamuseService;

    @Inject
    public WordService(Jdbi jdbi, DatamuseService datamuseService) {
        this.repository = new WordRepository(jdbi);
        this.datamuseService = datamuseService;
    }

    public List<Word> getAll() {
        return repository.findAll(100, 0);
    }

    public Optional<Word> getById(String id) {
        return repository.findById(UUID.fromString(id));
    }

    public Optional<Word> getByWord(String wordValue, boolean incRelated) {
        Optional<Word> optionalWord = repository.findByValue(wordValue, incRelated);
        if (CHECK_SYNONYMS_FROM_EXT && incRelated && optionalWord.isPresent()) {
            Word word = optionalWord.get();
            if (word.getLastExtCheck() == null) {
                updateAssociationsFromExtService(word);
                word.setLastExtCheck(ZonedDateTime.now());
                update(word);

            }
        }

        return optionalWord;
    }

    public List<Tuple> findAssociatedWords(UUID id) {
        return repository.findAssociations(id);
    }

    public List<Tuple> findAntonyms(UUID id) {
        List<Tuple> antList = repository.findAntonyms(id);
        if (antList.size() == 0 && CHECK_ANTONYMS_FROM_EXT){

        }
        return repository.findAntonyms(id);
    }

    public Word add(String word, WordType wordType) throws DocumentExists {
        WordDTO wordDTO = new WordDTO();
        wordDTO.setValue(word);
        wordDTO.setType(wordType.getAlias());
        return add(wordDTO);
    }

    public int updateRates(String id, String associatedWord, String rateAsString) {
        int rate = Integer.parseInt(rateAsString);
        return repository.updateRates(UUID.fromString(id), RatingType.EMPHASIS, associatedWord, rate );

    }
    public Word add(WordDTO dto) throws DocumentExists {
        Optional<Word> wordOptional = repository.findByValue(dto.getValue(), false);
        if (wordOptional.isEmpty()) {
            List<Word> relatedWords = new ArrayList<>();
            Map<String, AssociationDTO> associationsList = dto.getAssociations();
            if (associationsList != null) {
                for (AssociationDTO a : dto.getAssociations().values()) {
                    String value = a.getValue();
                    Optional<Word> associationOptional = repository.findByValue(value, false);
                    if (associationOptional.isPresent()) {
                        relatedWords.add(associationOptional.get());
                    } else {
                        Word associated = new Word.Builder()
                                .setValue(value)
                                .build();
                        Word associatedAdjective = repository.insert(associated);
                        relatedWords.add(associatedAdjective);
                    }
                }
            }
            Word word = new Word.Builder()
                    .setValue(dto.getValue())
                    .setAssociations(relatedWords)
                    .setType(WordType.getType(dto.getType()))
                    .build();
            return repository.insert(word);
        } else {
            throw new DocumentExists(dto.getValue());
        }
    }

    public WordType getWordType(String wordValue) throws DocumentExists {
        WordType wordType = cache.get(wordValue);
        if (wordType == null) {
            Optional<Word> wordOptional = getByWord(wordValue, false);
            if (wordOptional.isPresent()) {
                cache.put(wordValue, wordType);
                return wordOptional.get().getType();
            } else {
                WordType wordTypeFromExt = datamuseService.getWordType(wordValue);
                if (wordTypeFromExt != null) {
                    wordType = add(wordValue, wordTypeFromExt).getType();
                } else {
                    wordType = WordType.UNKNOWN;
                }
                cache.put(wordValue, wordType);
                return wordType;
            }
        }
        return wordType;
    }

    public void update(Word word) {
        repository.update(word);
    }

    public ProcessFeedback delete(String id) {
        ProcessFeedback feedback = new ProcessFeedback();
        if (id.equals("all")) {
            List<Word> wordList = repository.findAll(0, 0);
            for (Word word : wordList) {
                feedback.addEntry(buildFeedBackEntry(word.getId().toString(), repository.bareDelete(word)));
            }
        } else {
            Optional<Word> wordOptional = repository.findById(UUID.fromString(id));
            if (wordOptional.isPresent()) {
                feedback.addEntry(buildFeedBackEntry(id, repository.bareDelete(wordOptional.get())));
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



    private boolean updateAssociationsFromExtService(Word entity) {
        List<DatamuseWordDTO> resp = datamuseService.getSynonyms(entity.getValue());
        if (resp.size() > 0) {
            entity.setAssociations(resp.stream().map(v -> (new Word.Builder().setValue(v.getWord()))
                    .setType(datamuseService.getWordType(v.getWord()))
                    .build()).collect(Collectors.toList()));
            return true;
        } else {
            LOGGER.warn("There is no associations for \"" + entity.getValue() + "\"");
        }
        return false;
    }

    private boolean updateAntonymsFromExtService(Word entity) {
        List<DatamuseWordDTO> resp = datamuseService.getAntonyms(entity.getValue());
        if (resp.size() > 0) {
            entity.setAssociations(resp.stream().map(v -> (new Word.Builder().setValue(v.getWord()))
                    .setType(datamuseService.getWordType(v.getWord()))
                    .build()).collect(Collectors.toList()));
            return true;
        } else {
            LOGGER.warn("There is no antonyms for \"" + entity.getValue() + "\"");
        }
        return false;
    }

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

    public static void resetCache() {
        cache.clear();
        Arrays.stream(sysWords).forEach(v -> cache.put(v, WordType.SYSTEM));
    }

    static {
        cache = new HashMap<>();
        Arrays.stream(sysWords).forEach(v -> cache.put(v, WordType.SYSTEM));
    }


}
