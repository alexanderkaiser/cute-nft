package net.kal.cute.server.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import net.kal.cute.data.request.MatingRequestAnswerData;
import net.kal.cute.data.request.MatingRequestData;
import net.kal.cute.data.response.MatingRequestIdentifierResponse;
import net.kal.cute.server.ctx.AppContext;

@Slf4j
@Path("/mating")
public class MatingResource {

  @Inject AppContext context;

  @POST
  @Path("request")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public MatingRequestIdentifierResponse matingRequest(
      @HeaderParam("pub-key") String publicKey, MatingRequestData matingRequest) {
    return context.getMatingService().makeRequest(publicKey, matingRequest);
  }

  @POST
  @Path("answer")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public MatingRequestIdentifierResponse matingRequestResponse(
      @HeaderParam("pub-key") String publicKey, MatingRequestAnswerData matingRequestAnswer) {
    return context.getMatingService().responseRequest(publicKey, matingRequestAnswer);
  }
}
