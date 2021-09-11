package com.semantyca.controller;

import com.semantyca.dto.WordDTO;
import com.semantyca.dto.document.DocumentOutcome;
import com.semantyca.model.Word;
import com.semantyca.repository.exception.DocumentExists;
import com.semantyca.service.WordService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Singleton
@Path("/words")
public class WordController {

    @Inject
    private WordService wordService;

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
        Optional<Word> optionalWord = wordService.getByWord(word);
        if (optionalWord.isPresent()) {
            DocumentOutcome outcome = new DocumentOutcome();
            outcome.setPayload(optionalWord.get());
            return Response.ok().entity(outcome).build();
       } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Path("/")
    public Word create(WordDTO word) throws DocumentExists {
        return wordService.add(word);
    }

    @PUT
    @Path("/{id}/emphasis/{word}/{rate}")
    public Response updateEmphsis(@PathParam("id") String id, @PathParam("word") String word, @PathParam("rate") String rate) throws DocumentExists {
        return Response.ok().entity( wordService.updateEmphasisRank(id, word, rate)).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteWord(@PathParam("id") String id)  {
        return Response.ok().entity( wordService.delete(id)).build();
    }


        /*@POST
    @Path("/")
    public Uni<Response> create(WordDTO word) {
        return wordService.save(word)
                .onItem().transform(id -> URI.create("/words/" + id))
                .onItem().transform(uri -> Response.created(uri).build());
    }*/
}
