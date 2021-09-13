package com.semantyca.projectw.client;


import com.semantyca.projectw.dto.DatamuseWordDTO;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/words")
@RegisterRestClient(configKey = "datamuse")
public interface DatamuseConnection {

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    List<DatamuseWordDTO> get(@QueryParam("rel_syn") String word);

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    List<DatamuseWordDTO>  getWordType(@QueryParam("sp") String word, @QueryParam("md") String p);
}
