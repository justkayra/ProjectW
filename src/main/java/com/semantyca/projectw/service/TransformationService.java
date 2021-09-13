package com.semantyca.projectw.service;

import com.semantyca.projectw.dto.LegendEntryDTO;
import com.semantyca.projectw.dto.SlateTextElementDTO;
import com.semantyca.projectw.dto.TransformationRequestDTO;
import com.semantyca.projectw.dto.TransformationResultDTO;
import com.semantyca.projectw.dto.constant.EmphasisType;
import com.semantyca.projectw.dto.constant.WordType;
import com.semantyca.projectw.model.Word;
import com.semantyca.projectw.repository.exception.DocumentExists;
import com.semantyca.projectw.util.NumberUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TransformationService {
    private static final Logger LOGGER = LoggerFactory.getLogger("TransformationService");

    private WordService wordService;

    @Inject
    public TransformationService(WordService wordService) {
        this.wordService = wordService;
    }

    public TransformationResultDTO process(TransformationRequestDTO dto) throws DocumentExists {
        List<SlateTextElementDTO> elements = new ArrayList<>();
        List<LegendEntryDTO> legends = new ArrayList<>();
        String[] fields = preprocessText(dto.getSourceText());
        Mode mode = Mode.SKIPPING;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            String currentWord = fields[i].replace("\n", "");
            if (currentWord.length() > 0) {
                if (wordService.getWordType(currentWord) == WordType.ADJECTIVE) {
                    if (mode != Mode.REPLACING) {
                        String t = stringBuilder.toString();
                        if (!t.equals("")) {
                            elements.add(new SlateTextElementDTO.Builder().build(stringBuilder.toString()));
                            stringBuilder = new StringBuilder();
                        }
                    }
                    mode = Mode.REPLACING;
                    Word word = wordService.getByWord(currentWord, true).get();

                    List<Word> associationList = word.getAssociations();
                    if (associationList.size() > 0) {
                        String replacement = getReplacement(dto.getEmphasisType(), associationList);
                        stringBuilder.append(replacement);
                        legends.add(new LegendEntryDTO.Builder().setOldWord(currentWord).setNewWord(replacement).build());
                    } else {
                        stringBuilder.append(currentWord);
                    }
                } else {
                    if (mode != Mode.SKIPPING) {
                        String t = stringBuilder.toString();
                        if (!t.equals("")) {
                            elements.add(new SlateTextElementDTO.Builder().build(stringBuilder.toString(), true));
                            stringBuilder = new StringBuilder();
                        }
                    }
                    mode = Mode.SKIPPING;
                    stringBuilder.append(currentWord);
                }
            }
        }
        SlateTextElementDTO element = new SlateTextElementDTO.Builder().build(stringBuilder.toString());
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


    private static String[] preprocessText(String text) {
        return text
                .replace("\uFEFF", "")
                .replace(" ", "~&#32;~")
                .replace(",", "~&#44;~")
                .replace(".", "~&#46;~")
                .replace("\"", "~&#34;~")
                .split("~");
    }

    enum Mode {
        REPLACING, SKIPPING;
    }

}

