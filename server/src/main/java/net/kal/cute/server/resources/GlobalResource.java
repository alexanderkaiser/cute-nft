package net.kal.cute.server.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import net.kal.cute.data.response.OctopusOwnership;
import net.kal.cute.data.response.TransactionEnvelope;
import net.kal.cute.data.response.UserAccountData;
import net.kal.cute.server.ctx.AppContext;

@Slf4j
@Path("cute")
public class GlobalResource {

  @Inject
  AppContext context;

  @GET
  @Path("transactions")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public List<TransactionEnvelope> history() {
    return context.getTransactionService().getTransactions();
  }

  @GET
  @Path("octopuses")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public List<OctopusOwnership> octopuses() {
    return context.getOctopusService().getOctopusOwnerships();
  }

  @GET
  @Path("octopus/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Optional<OctopusOwnership> octopusById(@PathParam("id") String id) {
    return context.getOctopusService().getOctopusOwnershipById(id);
  }

  @GET
  @Path("users")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public List<UserAccountData> users() {
    return context.getUserService().getUsers().values().stream().toList();
  }
}
