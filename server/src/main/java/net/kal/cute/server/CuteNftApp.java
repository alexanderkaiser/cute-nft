package net.kal.cute.server;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import lombok.val;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api")
public class CuteNftApp extends Application {

  public CuteNftApp() {
    val cfg = ResourceConfig.forApplication(this);
    cfg.register(new AppBinder());
  }
}