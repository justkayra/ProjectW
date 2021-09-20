package com.semantyca.projectw.service;

import com.semantyca.projectw.dto.LegendEntryDTO;
import com.semantyca.projectw.dto.TransformationRequestDTO;
import com.semantyca.projectw.dto.constant.EmphasisType;
import com.semantyca.projectw.dto.constant.ReplacementResultType;
import com.semantyca.projectw.dto.constant.ResponseStyle;
import com.semantyca.projectw.dto.constant.WordType;
import com.semantyca.projectw.dto.delta.Attribute;
import com.semantyca.projectw.dto.delta.Entry;
import com.semantyca.projectw.model.Word;
import com.semantyca.projectw.repository.exception.DocumentExists;
import com.semantyca.projectw.util.NumberUtil;
import io.vertx.sqlclient.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class TransformationService {
    private static final Logger LOGGER = LoggerFactory.getLogger("TransformationService");

    private WordService wordService;

    @Inject
    public TransformationService(WordService wordService) {
        this.wordService = wordService;
    }

    public Object[] process(TransformationRequestDTO dto, ResponseStyle style) throws DocumentExists {
        List<Entry> ops = new ArrayList<>();
        List<LegendEntryDTO> legends = new ArrayList<>();
        String[] fields = preprocessText(dto.getSourceText());
        Mode mode = Mode.SKIPPING;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            String currentWord = fields[i].replace("\n", "");
            if (currentWord.length() > 0) {
                if (wordService.getWordType(currentWord) == WordType.ADJECTIVE) {
                    if (style == ResponseStyle.DELTA && mode != Mode.REPLACING) {
                        String t = stringBuilder.toString();
                        if (!t.equals("")) {
                            Entry entry = new Entry();
                            entry.setInsert(stringBuilder.toString());
                            ops.add(entry);
                            stringBuilder = new StringBuilder();
                        }
                    }
                    mode = Mode.REPLACING;
                    if (dto.getEmphasisType() == EmphasisType.STRONGER || dto.getEmphasisType() == EmphasisType.WEAKER || dto.getEmphasisType() == EmphasisType.RANDOMLY) {
                        Word word = wordService.getByWord(currentWord, true).get();
                        List<Tuple> assWords = wordService.findAssociatedWords(word.getId());
                        //List<Word> associationList = word.getAssociations();
                        if (assWords.size() > 0) {
                            Optional<String> stringOptional = getReplacement(dto.getEmphasisType(), assWords);
                            if (stringOptional.isPresent()) {
                                stringBuilder.append(stringOptional.get());
                                legends.add(new LegendEntryDTO.Builder().setOldWord(currentWord).setNewWord(stringOptional.get()).build());
                            } else {
                                stringBuilder.append(currentWord);
                                legends.add(new LegendEntryDTO.Builder().setOldWord(currentWord).setNewWord(currentWord).setResult(ReplacementResultType.KEPT).build());
                            }
                        } else {
                            stringBuilder.append(currentWord);
                        }
                    } else if(dto.getEmphasisType() == EmphasisType.OPPOSITE){
                        Word word = wordService.getByWord(currentWord, false).get();
                        List<Tuple> antonyms = wordService.findAntonyms(word.getId());

                    }
                } else {
                    if (style == ResponseStyle.DELTA && mode != Mode.SKIPPING) {
                        String t = stringBuilder.toString();
                        if (!t.equals("")) {
                            Entry entry = new Entry();
                            entry.setInsert(stringBuilder.toString());
                            Attribute attribute = new Attribute();
                            attribute.setBold(true);
                            entry.setAttributes(attribute);
                            ops.add(entry);
                            stringBuilder = new StringBuilder();
                        }
                    }
                    mode = Mode.SKIPPING;
                    stringBuilder.append(currentWord);
                }
            }
        }
        Entry entry = new Entry();
        entry.setInsert(stringBuilder.toString());
        if (style == ResponseStyle.DELTA && mode == Mode.REPLACING) {
            Attribute attribute = new Attribute();
            attribute.setBold(true);
            entry.setAttributes(attribute);
        }
        ops.add(entry);
        Object[] result =  {ops, legends};
        return result;
    }

    private Optional<String> getReplacement(EmphasisType emphasisType, List<Tuple> associationList) {
        UUID id = null;
        if (emphasisType == EmphasisType.RANDOMLY) {
            int index = NumberUtil.getRandomNumber(0, associationList.size() - 1);
            id = associationList.get(index).getUUID(0);
        } else if(emphasisType == EmphasisType.STRONGER) {
            List<Tuple> strongerWordsList = associationList.stream().filter(w -> w.getInteger(2) > 0).collect(Collectors.toList());
            if (strongerWordsList.size() > 0) {
                int index = NumberUtil.getRandomNumber(0, strongerWordsList.size() - 1);
                id = strongerWordsList.get(index).getUUID(0);
            } else {
                return Optional.empty();
            }
        } else if(emphasisType == EmphasisType.WEAKER) {
            List<Tuple> strongerWordsList = associationList.stream().filter(w -> w.getInteger(2) < 0).collect(Collectors.toList());
            if (strongerWordsList.size() > 0) {
                int index = NumberUtil.getRandomNumber(0, strongerWordsList.size() - 1);
                id = strongerWordsList.get(index).getUUID(0);
            } else {
                return Optional.empty();
            }
        }
        Optional<Word> wordOptional = wordService.getById(id);
        if (wordOptional.isPresent()){
            return Optional.of(wordOptional.get().getValue());
        } else {
            return Optional.empty();
        }

    }


    private static String[] preprocessText(String text) {
        return text
                .replace("\uFEFF", "")
                .replace("'", "~&#39;~")
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

