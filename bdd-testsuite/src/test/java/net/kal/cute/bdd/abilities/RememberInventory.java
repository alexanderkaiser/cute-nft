package net.kal.cute.bdd.abilities;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.serenitybdd.screenplay.Ability;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RememberInventory implements Ability {


  public static RememberInventory forHisAccount() {
    return new RememberInventory();
  }
}
