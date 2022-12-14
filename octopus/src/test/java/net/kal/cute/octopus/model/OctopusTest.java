package net.kal.cute.octopus.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lombok.val;
import org.junit.jupiter.api.Test;

class OctopusTest {

  @Test
  void shouldHaveMinMaleFitness() {
    val g = new Octopus("Praealtus", 0L);
    assertEquals(0.0, g.getFitness());
    assertEquals(Gender.MALE, g.getGender());
  }

  @Test
  void shouldHaveMinFemaleFitness() {
    val g = new Octopus("Eledone", Long.MIN_VALUE);
    assertEquals(0.0, g.getFitness());
    assertEquals(Gender.FEMALE, g.getGender());
  }

  @Test
  void shouldHaveMaxMaleFitness() {
    val g = new Octopus("Praealtus", Long.MAX_VALUE);
    assertEquals(100.0, g.getFitness());
    assertEquals(Gender.MALE, g.getGender());
  }

  @Test
  void shouldHaveMaxFemaleFitness() {
    //        val gen = Long.MIN_VALUE | Long.MAX_VALUE; => -1
    val g = new Octopus("Eledone", -1L);
    assertEquals(100.0, g.getFitness());
    assertEquals(Gender.FEMALE, g.getGender());
  }
}
