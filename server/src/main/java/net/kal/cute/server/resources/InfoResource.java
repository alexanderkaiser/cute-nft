package net.kal.cute.server.resources;

import static java.text.MessageFormat.format;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import net.kal.cute.server.CuteNftApp;
import net.kal.cute.server.ctx.AppContext;

@Path("/info")
public class InfoResource {

  @Inject AppContext context;

  @GET
  @Produces("text/plain")
  public String hello() {
    return format("Hello, World: v{0}", CuteNftApp.class.getPackage().getImplementationVersion());
  }
}
