package com.semantyca.controller;

import com.semantyca.dto.AdjectiveDTO;
import com.semantyca.dto.constant.OutcomeType;
import com.semantyca.dto.document.DocumentOutcome;
import com.semantyca.dto.error.ErrorOutcome;
import com.semantyca.model.Adjective;
import com.semantyca.repository.exception.DocumentExists;
import com.semantyca.service.AdjectiveService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Singleton
@Path("/adjectives")
public class AdjectiveController {

    @Inject
    private AdjectiveService adjectiveService;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Adjective> getWords() {
        return adjectiveService.get();
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addWord(AdjectiveDTO dto) {
        try {
            DocumentOutcome outcome = new DocumentOutcome();
            outcome.setPayload(adjectiveService.add(dto));
            return Response.ok().entity(outcome).build();
        } catch (DocumentExists e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorOutcome(e, OutcomeType.SOFT_ERROR)).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response.ResponseBuilder deleteWord(@PathParam("id") String id)  {
        return Response.ok().entity( adjectiveService.delete(id));
    }


}
