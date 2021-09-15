package com.semantyca.projectw.controller;

import com.semantyca.projectw.dto.PageOutcome;
import com.semantyca.projectw.dto.TransformationRequestDTO;
import com.semantyca.projectw.dto.constant.ResponseStyle;
import com.semantyca.projectw.repository.exception.DocumentExists;
import com.semantyca.projectw.service.TransformationService;

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
        Object[] result = transformationService.process(dto, ResponseStyle.DELTA);
        outcome.addPayload("ops", result[0]);
        outcome.addPayload("legend", result[1]);
        return Response.ok().entity(outcome).build();
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response transform(TransformationRequestDTO dto) throws DocumentExists {
        PageOutcome outcome = new PageOutcome();
        outcome.addPayload(transformationService.process(dto, ResponseStyle.MARKDOWN));
        return Response.ok().entity(outcome).build();
    }



}
