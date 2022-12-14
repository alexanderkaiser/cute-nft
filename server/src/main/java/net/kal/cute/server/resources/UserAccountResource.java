package net.kal.cute.server.resources;

import com.github.javafaker.Faker;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.kal.cute.data.error.MessageError;
import net.kal.cute.data.request.MatingRequestData;
import net.kal.cute.data.request.UserRegistrationData;
import net.kal.cute.data.response.UserAccountData;
import net.kal.cute.octopus.model.Octopus;
import net.kal.cute.server.ctx.AppContext;

@Slf4j
@Path("/account")
public class UserAccountResource {

  @Inject AppContext context;

  @POST
  @Path("register")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public UserAccountData register(@HeaderParam("pub-key") String publicKey, UserRegistrationData registrationData) {
    if (context.getUserService().hasUser(publicKey)) {
      throw new WebApplicationException(
          Response.status(304).entity(MessageError.withMessage("User with given public key already exists")).build());
    }

    if (registrationData == null || registrationData.getUserName() == null) {
      registrationData = new UserRegistrationData();
      registrationData.setUserName(Faker.instance().funnyName().name());
    }

    return context.registerNewUser(publicKey, registrationData);
  }

  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public UserAccountData account(@HeaderParam("pub-key") String publicKey) {
    return context.getUserService().getUser(publicKey).orElseThrow(() -> new WebApplicationException(
        Response.status(404).entity(MessageError.withMessage("User with given public key does not exists")).build()));
  }

  @GET
  @Path("inventory")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public List<Octopus> inventory(@HeaderParam("pub-key") String publicKey) {
    return context.getOctopusService().getInventory(publicKey);
  }

  @GET
  @Path("mating/requests")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public List<MatingRequestData> matingrequests(@HeaderParam("pub-key") String publicKey) {
    return context.getMatingService().getRequestsForUser(publicKey);
  }
}
