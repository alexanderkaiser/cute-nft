package net.kal.cute.bdd.tasks;

import javax.annotation.Nullable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.val;
import net.kal.cute.bdd.abilities.UseTheCuteClient;
import net.kal.cute.client.rest.Registration;
import net.kal.cute.data.response.UserAccountData;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RegisterWithTheCuteService implements Performable {

  private final boolean useActorName;
  @Nullable private final String customUsername;

  @Override
  public <T extends Actor> void performAs(T actor) {
    val client = actor.abilityTo(UseTheCuteClient.class).getClient();

    UserAccountData response;
    if (useActorName) {
      response = client.request(Registration.withUsername(actor.getName()));
    } else {
      if (customUsername != null) {
        response = client.request(Registration.withUsername(customUsername));
      } else {
        response = client.request(Registration.withRandomUsername());
      }
    }

    Serenity.recordReportData().withTitle("Registration Public Key").andContents(
        response.getPublicKey());
  }

  public static RegisterWithTheCuteService withUsername() {
    return new RegisterWithTheCuteService(true, null);
  }

  public static RegisterWithTheCuteService withSystemGeneratedUsername() {
    return new RegisterWithTheCuteService(false, null);
  }

  public static Builder withCustomProperties() {
    return new Builder();
  }

  public static class Builder {
    private static final boolean useActorName = false;

    public Builder providingCustomName(String a) {
      return this;
    }

    public Builder withSeedForRSA(int a) {
      return this;
    }

    public RegisterWithTheCuteService withCustomUsername(String custom) {
      return new RegisterWithTheCuteService(useActorName, custom);
    }
  }
}
