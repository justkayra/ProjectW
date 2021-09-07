package com.semantyca.controller;

import com.semantyca.dto.PageOutcome;
import com.semantyca.dto.TransformationRequestDTO;
import com.semantyca.repository.exception.DocumentExists;
import com.semantyca.service.TransformationService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Singleton
@Path("/transform")
public class TransformationController {

    @Inject
    TransformationService transformationService;

    @POST
    @Path("/mood")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addWord(TransformationRequestDTO dto) throws DocumentExists {
        PageOutcome outcome = new PageOutcome();
        outcome.addPayload(transformationService.process(dto));
        return Response.ok().entity(outcome).build();
    }

}
