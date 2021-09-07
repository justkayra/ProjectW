package com.semantyca.service;

import com.semantyca.dto.AdjectiveDTO;
import com.semantyca.dto.DatamuseWordDTO;
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
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;
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

    public String process(TransformationRequestDTO dto) throws DocumentExists {
        String sourceText = dto.getSourceText();
        if (dto.getEmphasis() == EmphasisType.RANDOM) {
            StringTokenizer st = new StringTokenizer(sourceText, " ");
            while (st.hasMoreTokens()) {
                String value = st.nextToken();
                WordType wordType = datamuseService.getWordType(value);
                if (wordType == WordType.ADJECTIVE) {
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
                        sourceText = sourceText.replace(value, replacement);
                    }
                }
            }
        }
        return sourceText;
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
        entity.setAssociations(resp.stream().map(v -> (new Adjective.Builder().setValue(v.getWord())).build()).collect(Collectors.toList()));
        adjectiveRepository.update(entity);
    }

}

