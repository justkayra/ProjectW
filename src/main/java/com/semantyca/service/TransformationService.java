package com.semantyca.service;

import com.semantyca.dto.AdjectiveDTO;
import com.semantyca.dto.DatamuseWordDTO;
import com.semantyca.dto.SlateTextElementDTO;
import com.semantyca.dto.TransformationRequestDTO;
import com.semantyca.dto.constant.EmphasisType;
import com.semantyca.dto.constant.WordType;
import com.semantyca.model.Adjective;
import com.semantyca.repository.AdjectiveRepository;
import com.semantyca.repository.exception.DocumentExists;
import com.semantyca.util.NumberUtil;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class TransformationService {
    private static final Logger LOGGER = LoggerFactory.getLogger("TransformationService");

    private AdjectiveRepository adjectiveRepository;

    private AdjectiveService adjectiveService;

    private DatamuseService datamuseService;

    @Inject
    public TransformationService(Jdbi jdbi, DatamuseService datamuseService, AdjectiveService adjectiveService) {
        adjectiveRepository = new AdjectiveRepository(jdbi);
        this.adjectiveService = adjectiveService;
        this.datamuseService = datamuseService;
    }

    public List<SlateTextElementDTO> process(TransformationRequestDTO dto) throws DocumentExists {
        List<SlateTextElementDTO> elements = new ArrayList<>();
        String sourceText = dto.getSourceText();
        sourceText = sourceText
                .replace("\uFEFF", "")
                .replace(" ", "~&#32;~")
                .replace(",", "~&#44;~")
                .replace(".", "~&#46;~");
        if (dto.getEmphasis() == EmphasisType.RANDOM) {
            String[] fields = sourceText.split("~");
            Mode mode = Mode.SKIPPING;
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < fields.length; i++) {
                String value = fields[i];
                if (value.length() > 0) {
                    WordType wordType = datamuseService.getWordType(value);
                    if (wordType == WordType.ADJECTIVE) {
                        if (mode != Mode.REPLACING) {
                            String t = builder.toString();
                            if (!t.equals("")) {
                                SlateTextElementDTO element = new SlateTextElementDTO();
                                element.setText(builder.toString());
                                elements.add(element);
                                builder = new StringBuilder();
                            }
                        }
                        mode = Mode.REPLACING;
                        Adjective adjective = null;
                        Optional<Adjective> val = adjectiveRepository.findByValue(value);
                        if (val.isEmpty()) {
                            adjective = persistAdjective(value);
                        } else {
                            adjective = val.get();
                            List<Adjective> associationList = adjective.getAssociations();
                            if (associationList.size() == 0) {
                                updateAdjectiveAssociations(adjective);
                            }
                        }
                        List<Adjective> associationList = adjective.getAssociations();
                        if (associationList.size() > 0) {
                            int randomIndex = NumberUtil.getRandomNumber(0, associationList.size() - 1);
                            String replacement = associationList.get(randomIndex).getValue();
                            builder.append(replacement);
                        } else {
                            builder.append(value);
                        }
                    } else {
                        if (mode != Mode.SKIPPING) {
                            String t = builder.toString();
                            if (!t.equals("")) {
                                SlateTextElementDTO element = new SlateTextElementDTO();
                                element.setBold(true);
                                element.setText(builder.toString());
                                elements.add(element);
                                builder = new StringBuilder();
                            }
                        }
                        mode = Mode.SKIPPING;
                        builder.append(value);
                    }
                }
            }
            SlateTextElementDTO element = new SlateTextElementDTO();
            element.setText(builder.toString());
            if (mode == Mode.REPLACING) {
                element.setBold(true);
            }
            elements.add(element);
        }
        return elements;
    }

    private Adjective persistAdjective(String value) throws DocumentExists {
        List<DatamuseWordDTO> resp = datamuseService.getWord(value);
        AdjectiveDTO adjectiveDTO = new AdjectiveDTO();
        adjectiveDTO.setValue(value);
        adjectiveDTO.setAssociations(resp.stream().map(v -> (v.getWord())).collect(Collectors.toList()));
        return adjectiveService.add(adjectiveDTO);
    }

    private void updateAdjectiveAssociations(Adjective entity) throws DocumentExists {
        List<DatamuseWordDTO> resp = datamuseService.getWord(entity.getValue());
        if (resp.size() > 0) {
            entity.setAssociations(resp.stream().map(v -> (new Adjective.Builder().setValue(v.getWord())).build()).collect(Collectors.toList()));
            adjectiveRepository.update(entity);
        } else {
            LOGGER.warn("There is no associations for \"" + entity.getValue() + "\"");
        }
    }

    enum Mode {
        REPLACING, SKIPPING;
    }

}

