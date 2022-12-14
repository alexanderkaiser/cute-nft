package net.kal.cute.octopus.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import lombok.val;
import org.junit.jupiter.api.Test;

class OctopusFactoryTest {

  @Test
  void shouldGenerateRandomlyWithCorrectGender() {
    val factory = new OctopusFactory();
    val genders = List.of(Gender.values());
    genders.forEach(g -> {
      val octo = factory.createRandomOctopus(g);
      assertEquals(g, octo.getGender());
    });
  }
}