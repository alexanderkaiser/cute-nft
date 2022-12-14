package net.kal.cute.bdd.steps;

import static java.text.MessageFormat.format;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.de.Angenommen;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.kal.cute.client.CuteClientImpl;
import net.kal.cute.client.rest.Registration;
import net.kal.cute.crypto.CryptoKeyFactory;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.actors.Cast;
import net.serenitybdd.screenplay.actors.OnStage;

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

  @Angenommen("^der Benutzer (.+) meldet sich an$")
  public void user(String username) {
    val theUser = OnStage.theActor(username);
    log.info(format("Perform some actions with {0}", username));

    val client1 =
        CuteClientImpl.forUrl("http://localhost:8080")
            .with(CryptoKeyFactory.generateRandomKeyPair())
            .andCustomSpec(SerenityRest::given);

    val response = client1.request(Registration.withUsername(username));
    log.info(format("Registration Response: {0}", response));
  }
}
