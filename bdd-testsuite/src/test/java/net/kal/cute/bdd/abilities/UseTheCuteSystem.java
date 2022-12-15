package net.kal.cute.bdd.abilities;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import net.kal.cute.CuteClient;
import net.kal.cute.CuteRequest;
import net.serenitybdd.screenplay.Ability;

@RequiredArgsConstructor
public class UseTheCuteSystem implements Ability {

  private final CuteClient client;

  public <R> R request(CuteRequest<R> request) {
    return client.request(request);
  }

  public static UseTheCuteSystem withCute(CuteClient client) {
    return new UseTheCuteSystem(client);
  }
}
