package com.semantyca.service;

import com.semantyca.dto.*;
import com.semantyca.dto.constant.EmphasisType;
import com.semantyca.dto.constant.WordType;
import com.semantyca.model.Word;
import com.semantyca.repository.WordRepository;
import com.semantyca.repository.exception.DocumentExists;
import com.semantyca.util.NumberUtil;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class TransformationService {
    private static final Logger LOGGER = LoggerFactory.getLogger("TransformationService");

    private WordRepository wordRepository;

    private WordService wordService;

    private DatamuseService datamuseService;

    @Inject
    public TransformationService(Jdbi jdbi, DatamuseService datamuseService, WordService wordService) {
        wordRepository = new WordRepository(jdbi);
        this.wordService = wordService;
        this.datamuseService = datamuseService;
    }

    public TransformationResultDTO process(TransformationRequestDTO dto) throws DocumentExists {
        List<SlateTextElementDTO> elements = new ArrayList<>();
        List<LegendEntryDTO> legends = new ArrayList<>();
        String sourceText = normalizeText(dto.getSourceText());
        String[] fields = sourceText.split("~");
        Mode mode = Mode.SKIPPING;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            String currentWord = fields[i];
            if (datamuseService.getWordType(currentWord) == WordType.ADJECTIVE) {
                if (mode != Mode.REPLACING) {
                    String t = builder.toString();
                    if (!t.equals("")) {
                        elements.add(new SlateTextElementDTO.Builder().build(builder.toString()));
                        builder = new StringBuilder();
                    }
                }
                mode = Mode.REPLACING;
                Word word;
                Optional<Word> val = wordRepository.findByValue(currentWord);
                if (val.isEmpty()) {
                    word = persistWord(currentWord);
                } else {
                    word = val.get();
                    if (word.getAssociations().size() == 0) {
                        updateAssociations(word);
                    }
                }
                List<Word> associationList = word.getAssociations();
                if (associationList.size() > 0) {
                    String replacement = getReplacement(dto.getEmphasisType(), associationList);
                    builder.append(replacement);
                    legends.add(new LegendEntryDTO.Builder().setOldWord(currentWord).setNewWord(replacement).build());
                } else {
                    builder.append(currentWord);
                }
            } else {
                if (mode != Mode.SKIPPING) {
                    String t = builder.toString();
                    if (!t.equals("")) {
                        elements.add(new SlateTextElementDTO.Builder().build(builder.toString(), true));
                        builder = new StringBuilder();
                    }
                }
                mode = Mode.SKIPPING;
                builder.append(currentWord);
            }

        }
        SlateTextElementDTO element = new SlateTextElementDTO.Builder().build(builder.toString());
        if (mode == Mode.REPLACING) {
            element.setBold(true);
        }
        elements.add(element);
        TransformationResultDTO result = new TransformationResultDTO();
        result.setType("paragraph");
        result.setChildren(elements);
        result.setLegendEntries(legends);
        return result;
    }

    private String getReplacement(EmphasisType emphasisType, List<Word> associationList) {
        int index = 0;
        if (emphasisType == EmphasisType.RANDOM) {
            index = NumberUtil.getRandomNumber(0, associationList.size() - 1);
        }
        return associationList.get(index).getValue();
    }

    private Word persistWord(String value) throws DocumentExists {
        List<DatamuseWordDTO> resp = datamuseService.getWord(value);
        WordDTO wordDTO = new WordDTO();
        wordDTO.setValue(value);
        wordDTO.setAssociations(resp.stream().map(v -> (v.getWord())).collect(Collectors.toList()));
        return wordService.add(wordDTO);
    }

    private void updateAssociations(Word entity) {
        List<DatamuseWordDTO> resp = datamuseService.getWord(entity.getValue());
        if (resp.size() > 0) {
            entity.setAssociations(resp.stream().map(v -> (new Word.Builder().setValue(v.getWord())).build()).collect(Collectors.toList()));
            wordRepository.update(entity);
        } else {
            LOGGER.warn("There is no associations for \"" + entity.getValue() + "\"");
        }
    }

    private static String normalizeText(String text) {
        return text
                .replace("\uFEFF", "")
                .replace(" ", "~&#32;~")
                .replace(",", "~&#44;~")
                .replace(".", "~&#46;~")
                .replace("\"", "~&#34;~");
    }

    enum Mode {
        REPLACING, SKIPPING;
    }

}

