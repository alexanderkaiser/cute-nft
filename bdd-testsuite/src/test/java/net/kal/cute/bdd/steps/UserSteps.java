package net.kal.cute.bdd.steps;

import static net.serenitybdd.screenplay.GivenWhenThen.givenThat;
import static net.serenitybdd.screenplay.GivenWhenThen.then;
import static net.serenitybdd.screenplay.GivenWhenThen.when;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.de.Angenommen;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.kal.cute.bdd.TestcaseData;
import net.kal.cute.bdd.abilities.RememberInventory;
import net.kal.cute.bdd.abilities.UseTheCuteClient;
import net.kal.cute.bdd.questions.AskForInventory;
import net.kal.cute.bdd.tasks.RegisterWithTheCuteService;
import net.serenitybdd.screenplay.actors.Cast;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.ensure.Ensure;

@Slf4j
public class UserSteps {

  private TestcaseData data;

  @Before
  public void setUp() {
    OnStage.setTheStage(Cast.ofStandardActors());
    data = new TestcaseData();
  }

  @After
  public void teardown() {
    OnStage.drawTheCurtain();
  }


  @Angenommen("^der Benutzer (.+) meldet sich an mit seinem Benutzernamen an$")
  public void user(String username) {
    val theUser = OnStage.theActor(username);

    givenThat(theUser.can(UseTheCuteClient.with(data.createClientFor(username))));
    givenThat(theUser.can(RememberInventory.forHisAccount()));

    when(theUser).attemptsTo(RegisterWithTheCuteService.withUsername());
  }

  @Angenommen("^der Benutzer (.+) meldet sich mit einem vom System generierten Benutzernamen an$")
  public void userRegisterWithRandomName(String username) {
    val theUser = OnStage.theActor(username);
    givenThat(theUser.can(UseTheCuteClient.with(data.createClient())));
    givenThat(theUser.can(RememberInventory.forHisAccount()));

    when(theUser).attemptsTo(RegisterWithTheCuteService.withSystemGeneratedUsername());
  }

  @Angenommen("^der Benutzer (.+) meldet sich mit dem Benutzername ''(.*)'' an$")
  public void userRegisterWithCustomName(String actorName, String userName) {
    val theUser = OnStage.theActor(actorName);
    givenThat(theUser.can(UseTheCuteClient.with(data.createClient())));
    givenThat(theUser.can(RememberInventory.forHisAccount()));

    when(theUser).attemptsTo(
        RegisterWithTheCuteService.withCustomProperties().providingCustomName("abc")
            .withSeedForRSA(123).withCustomUsername(userName));
  }
}
