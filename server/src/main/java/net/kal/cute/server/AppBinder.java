package net.kal.cute.server;

import net.kal.cute.server.ctx.AppContext;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class AppBinder extends AbstractBinder {

  @Override
  protected void configure() {
    bind(new AppContext()).to(AppContext.class);
  }
}
