package com.semantyca.projectw.facade;

import com.semantyca.projectw.dto.AssociationDTO;
import com.semantyca.projectw.dto.WordDTO;
import com.semantyca.projectw.model.Word;
import com.semantyca.projectw.service.WordService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApplicationScoped
public class WordServiceFacade {

    @Inject
    private WordService wordService;

    public WordDTO getWordById(String id) {
        Optional<Word> wordOptional = wordService.getById(id);
        if (wordOptional.isPresent()) {
            return buildDTO(wordOptional.get());
        }
        return null;
    }

    public WordDTO getWord(String value) {
        Optional<Word> wordOptional = wordService.getByWord(value, false);
        if (wordOptional.isPresent()) {
            return buildDTO(wordOptional.get());
        }
        return null;
    }

    private WordDTO buildDTO (Word word){
        WordDTO dto = new WordDTO();
        dto.setId(String.valueOf(word.getId()));
        dto.setValue(word.getValue());
        dto.setType(String.valueOf(word.getType()));

        Map<String, AssociationDTO> ass = wordService.findAssociatedWords(word.getId()).stream()
                .map(tuple -> {
            AssociationDTO associationDTO = new AssociationDTO();
            associationDTO.setId(String.valueOf(tuple.getUUID(0)));
            associationDTO.setValue(tuple.getString(1));
            associationDTO.setEmphasisRank(tuple.getInteger(2));
            return associationDTO;
        }).collect(Collectors.toMap(AssociationDTO::getId, Function.identity()));
        ass.put(String.valueOf(word.getId()), new AssociationDTO.Builder()
                .setValue(word.getValue())
                .setId(word.getId())
                .isBaseWord(true).build());
        dto.setAssociations(sortMap(ass));
        return dto;
    }

    public static LinkedHashMap sortMap(Map<String, AssociationDTO> map) {
        List<Map.Entry<String, AssociationDTO>> list = new LinkedList(map.entrySet());
        Collections.sort(list, Collections.reverseOrder(Comparator.comparingInt(dto -> dto.getValue().getEmphasisRank())));
        LinkedHashMap<String, AssociationDTO> result = new LinkedHashMap();
        for (Map.Entry<String, AssociationDTO> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }


}
