package com.semantyca.controller;

import com.semantyca.dto.WordDTO;
import com.semantyca.model.Word;
import com.semantyca.service.WordService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Singleton
@Path("/words")
public class WordController {

    @Inject
    private WordService wordService;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<Word> getWords() {
        return wordService.get();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Word> getWord(@PathParam("id") String id) {
        return wordService.get(id);
    }

    @POST
    @Path("/")
    public Uni<Response> create(WordDTO word) {
        return wordService.save(word)
                .onItem().transform(id -> URI.create("/words/" + id))
                .onItem().transform(uri -> Response.created(uri).build());
    }

}
