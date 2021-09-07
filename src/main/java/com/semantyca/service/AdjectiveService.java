package com.semantyca.service;

import com.semantyca.dto.AdjectiveDTO;
import com.semantyca.dto.FeedbackEntry;
import com.semantyca.dto.ProcessFeedback;
import com.semantyca.dto.constant.MessageLevel;
import com.semantyca.model.Adjective;
import com.semantyca.repository.AdjectiveRepository;
import com.semantyca.repository.exception.DocumentExists;
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
public class AdjectiveService {
    private static final Logger LOGGER = LoggerFactory.getLogger("WordService");
    private AdjectiveRepository adjectiveRepository;

    @Inject
    public AdjectiveService(Jdbi jdbi) {
        adjectiveRepository = new AdjectiveRepository(jdbi);
    }

    public List<Adjective> get() {
        return adjectiveRepository.findAllUnrestricted(100, 0);
    }

    public Adjective get(String word) {
        return adjectiveRepository.findByValue(word).get();
    }

    public Adjective add(AdjectiveDTO dto) throws DocumentExists {
        Optional<Adjective> wordOptional = adjectiveRepository.findByValue(dto.getValue());
        if (wordOptional.isEmpty()) {
            List<Adjective> relatedAdjectives = new ArrayList<>();
            List<String> associationsList = dto.getAssociations();
            if (associationsList != null) {
                for (String a : dto.getAssociations()) {
                    Optional<Adjective> association = adjectiveRepository.findByValue(a);
                    if (association.isPresent()) {
                        relatedAdjectives.add(association.get());
                    } else {
                        Adjective associatedAdj = new Adjective.Builder()
                                .setValue(a)
                                .build();
                        Adjective associatedAdjective = adjectiveRepository.insert(associatedAdj);
                        relatedAdjectives.add(associatedAdjective);
                    }
                }
            }
            Adjective adjective = new Adjective.Builder()
                    .setValue(dto.getValue())
                    .setAssociations(relatedAdjectives)
                    .build();
            return adjectiveRepository.insert(adjective);
        } else {
            throw new DocumentExists(dto.getValue());
        }
    }

    public ProcessFeedback delete(String id) {
        ProcessFeedback feedback = new ProcessFeedback();
        if (id.equals("all")) {
            List<Adjective> adjectiveList = adjectiveRepository.findAllUnrestricted(0, 0);
            for (Adjective adjective : adjectiveList) {
                feedback.addEntry(buildFeedBackEntry(id, adjectiveRepository.bareDelete(adjective)));
            }
        } else {
            Optional<Adjective> wordOptional = adjectiveRepository.findById(UUID.fromString(id));
            if (wordOptional.isPresent()) {
                feedback.addEntry(buildFeedBackEntry(id, adjectiveRepository.bareDelete(wordOptional.get())));
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
