package com.semantyca.service;

import com.semantyca.dto.TransformationRequestDTO;
import com.semantyca.model.Adjective;
import com.semantyca.repository.exception.DocumentExists;
import io.quarkus.test.Mock;
import io.quarkus.test.junit.QuarkusTest;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
class TransformationServiceTest {
    private static final String example = "Powerful and user-friendly Continuous Integration " +
            "and Deployment server that works out of the box";
    @Mock
    Jdbi jdbi;


    @Mock
    DatamuseService datamuseService;

    @Mock
    AdjectiveService adjectiveService;

    @Test
    void testProcess() throws DocumentExists {
        Optional<Adjective> adjective = Optional.of(new Adjective.Builder().setValue("powerful").build());
        Mockito.when(jdbi.withHandle(any(HandleCallback.class))).thenReturn(adjective);
        TransformationService service = new TransformationService(jdbi, datamuseService, adjectiveService);
        TransformationRequestDTO requestDTO = new TransformationRequestDTO.Builder()
                .setSourceText(example)
                .build();

        String result = service.process(requestDTO);
        assertNotNull(result);

    }

}
