package net.kal.cute.bdd.abilities;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.kal.cute.CuteClient;
import net.serenitybdd.screenplay.Ability;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.HasTeardown;
import net.serenitybdd.screenplay.RefersToActor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UseTheCuteClient implements Ability, HasTeardown, RefersToActor {

  private Actor owner;
  private final CuteClient client;

  @Setter
  private byte[] authCertificate;

  public static UseTheCuteClient with(CuteClient client) {
    return new UseTheCuteClient(client);
  }

  @Override
  public void tearDown() {
    System.out.println("###> Teardown the CuteClient for " + owner.getName());
  }

  @Override
  public <T extends Ability> T asActor(Actor actor) {
    this.owner = actor;
    return (T)this;
  }
}
