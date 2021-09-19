package com.semantyca.projectw.controller;

import com.semantyca.projectw.dto.WordDTO;
import com.semantyca.projectw.dto.document.DocumentOutcome;
import com.semantyca.projectw.facade.WordServiceFacade;
import com.semantyca.projectw.model.Word;
import com.semantyca.projectw.repository.exception.DocumentExists;
import com.semantyca.projectw.service.WordService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Singleton
@Path("/words")
public class WordController {

    @Inject
    private WordService wordService;

    @Inject
    WordServiceFacade wordServiceFacade;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Word> getWords() {
        return wordService.getAll();
    }


    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Word getWordById(@PathParam("id") String id) {
        return wordService.getById(id).get();
    }

    @GET
    @Path("/value/{word}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWord(@PathParam("word") String word) {
        WordDTO wordDTO = wordServiceFacade.getWord(word);
        DocumentOutcome outcome = new DocumentOutcome();
        outcome.addPayload(wordDTO);
        return Response.ok().entity(outcome).build();
    }

    @POST
    @Path("/")
    public Word create(WordDTO word) throws DocumentExists {
        return wordService.add(word);
    }

    @PUT
    @Path("/{id}/emphasis/{word}/{rate}")
    public Response updateEmphsis(@PathParam("id") String id, @PathParam("word") String word, @PathParam("rate") String rate) {
        wordService.updateRates(id, word, rate);
        WordDTO wordDTO = wordServiceFacade.getWordById(id);
        DocumentOutcome outcome = new DocumentOutcome();
        outcome.addPayload(wordDTO);
        return Response.ok().entity(outcome).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteWord(@PathParam("id") String id) {
        return Response.ok().entity(wordService.delete(id)).build();
    }


        /*@POST
    @Path("/")
    public Uni<Response> create(WordDTO word) {
        return wordService.save(word)
                .onItem().transform(id -> URI.create("/words/" + id))
                .onItem().transform(uri -> Response.created(uri).build());
    }*/
}
