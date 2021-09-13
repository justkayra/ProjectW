package com.semantyca.projectw.service;

import com.semantyca.projectw.repository.exception.DocumentExists;
import io.quarkus.test.Mock;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.Test;


class TransformationServiceTest {
    private static final String example = "Powerful and user-friendly Continuous Integration " +
            "and Deployment server that works out of the box";
    @Mock
    Jdbi jdbi;


    @Mock
    DatamuseService datamuseService;


    @Test
    void testProcess() throws DocumentExists {


    }

}
