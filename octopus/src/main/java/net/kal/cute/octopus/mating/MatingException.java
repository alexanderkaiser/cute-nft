package net.kal.cute.octopus.mating;

import static java.text.MessageFormat.format;

import net.kal.cute.octopus.model.Octopus;

public class MatingException extends RuntimeException {

  public MatingException(Octopus first, Octopus second) {
    super(format("It is not allowed to mate the octopuses {0} and {1}", first, second));
  }
}
