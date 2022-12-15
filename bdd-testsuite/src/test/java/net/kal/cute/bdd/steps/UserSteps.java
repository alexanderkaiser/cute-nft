package net.kal.cute.bdd.steps;

import static net.serenitybdd.screenplay.GivenWhenThen.givenThat;
import static net.serenitybdd.screenplay.GivenWhenThen.when;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.de.Angenommen;
import io.cucumber.java.de.Wenn;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.kal.cute.bdd.abilities.UseTheCuteSystem;
import net.kal.cute.bdd.task.RegisterOnServer;
import net.kal.cute.client.CuteClientImpl;
import net.kal.cute.crypto.CryptoKeyFactory;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.actors.Cast;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.ensure.Ensure;

@Slf4j
public class UserSteps {

  @Before
  public void setUp() {
    OnStage.setTheStage(Cast.ofStandardActors());
  }

  @After
  public void teardown() {
    OnStage.drawTheCurtain();
  }

  @Angenommen("^(?:der Benutzer|die Benutzerin) (.+) ist noch nicht registriert$")
  public void givenNotRegistered(String name) {
    val theUser = OnStage.theActor(name);
    val nonAbility = theUser.abilityTo(UseTheCuteSystem.class);
    Ensure.that(nonAbility == null);

    givenThat(theUser).can(
        UseTheCuteSystem.withCute(
            CuteClientImpl.forUrl("http://localhost:8080")
                .with(CryptoKeyFactory.generateRandomKeyPair())
                .andCustomSpec(SerenityRest::given)));
  }

  @Wenn("^(?:der Benutzer|die Benutzerin) (.+) sich mit dem Benutzernamen \"(.+)\" registriert")
  public void whenRegisterWithUsername(String name, String username) {
    val theUser = OnStage.theActor(name);
    when(theUser).attemptsTo(RegisterOnServer.withUsername(username));
  }

  @Wenn("^(?:der Benutzer|die Benutzerin) (.+) sich ohne Benutzernamen registriert")
  public void whenRegisterWithoutUsername(String name) {
    val theUser = OnStage.theActor(name);
    when(theUser).attemptsTo(RegisterOnServer.withRandomUsername());
  }

  @Wenn("^(?:der Benutzer|die Benutzerin) (.+) sich mit seinem Namen registriert")
  public void whenRegisterWitUsername(String name) {
    val theUser = OnStage.theActor(name);
    when(theUser).attemptsTo(RegisterOnServer.withMyName());
  }

  //  @Angenommen("^der Benutzer (.+) meldet sich an$")
  //  public void user(String username) {
  //    val theUser = OnStage.theActor(username);
  //    log.info(format("Perform some actions with {0}", username));
  //
  //    val client1 =
  //        CuteClientImpl.forUrl("http://localhost:8080")
  //            .with(CryptoKeyFactory.generateRandomKeyPair())
  //            .andCustomSpec(SerenityRest::given);
  //
  //    val response = client1.request(Registration.withUsername(username));
  //    log.info(format("Registration Response: {0}", response));
  //  }
}
