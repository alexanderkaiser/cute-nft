package net.kal.cute.bdd.task;

import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.val;
import net.kal.cute.bdd.abilities.UseTheCuteSystem;
import net.kal.cute.client.rest.Registration;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;

@RequiredArgsConstructor
public class RegisterOnServer implements Task {

  @Nullable private final String username;
  private final boolean userActorName;

  public static RegisterOnServer withUsername(String username) {
    return new RegisterOnServer(username, false);
  }

  public static RegisterOnServer withMyName() {
    return new RegisterOnServer(null, true);
  }

  public static RegisterOnServer withRandomUsername() {
    return new RegisterOnServer(null, false);
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    val ability = actor.abilityTo(UseTheCuteSystem.class);
    if (username == null) {
      if (!userActorName) {
        val response = ability.request(Registration.withRandomUsername());
      } else {
        val response = ability.request(Registration.withUsername(actor.getName()));
      }
    } else {
      val response = ability.request(Registration.withUsername(username));
    }
  }
}
