package net.kal.cute.octopus.model;


import com.github.javafaker.Faker;
import com.talanlabs.avatargenerator.Avatar;
import com.talanlabs.avatargenerator.smiley.SmileyAvatar;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.Random;
import lombok.val;
import net.kal.cute.octopus.mating.MatingException;
import net.kal.cute.octopus.mating.MatingStrategy;

public class OctopusFactory {

  private static final Avatar AVATAR_BUILDER = SmileyAvatar.newGhostAvatarBuilder().build();

  private final Random rnd;
  private final Faker faker;
  private final MatingStrategy matingStrategy;

  public OctopusFactory() {
    this(new SecureRandom());
  }

  public OctopusFactory(Random rnd) {
    this(rnd, MatingStrategy.Defaults.NAND.getStrategy());
    //        this(rnd, MatingStrategy.randomStrategy(rnd));
  }

  public OctopusFactory(Random rnd, MatingStrategy matingStrategy) {
    this.rnd = rnd;
    this.faker = new Faker(new Locale("de"), rnd);
    this.matingStrategy = matingStrategy;
  }

  public Octopus createRandomOctopus() {
    val name = faker.funnyName().name();
    val genome = rnd.nextLong();
    return new Octopus(name, genome);
  }

  public Octopus createRandomOctopus(Gender gender) {
    val name = faker.funnyName().name();


    long genome;
    if (gender == Gender.FEMALE) {
      genome = rnd.nextLong() | Long.MIN_VALUE;
    } else {
      genome = rnd.nextLong() & Long.MAX_VALUE;
    }

    return new Octopus(name, genome);
  }

  public Octopus mate(Octopus first, Octopus second) {
    // NOTE: skip this check for now to have an intended Bug!
//    if (!allowedToMate(first, second)) {
//      throw new MatingException(first, second);
//    }

    val offspringName = faker.funnyName().name();
    val offspringGenome = matingStrategy.mate(first.getGenome(), second.getGenome());
    return new Octopus(offspringName, offspringGenome);
  }

  public boolean allowedToMate(Octopus first, Octopus second) {
    return !first.getGender().equals(second.getGender());
  }

  public BufferedImage createAvatar(Octopus octopus) {
    val avatar = AVATAR_BUILDER.create(octopus.getGenome());
    val g = avatar.getGraphics();
    val font = new Font("Arial", Font.BOLD, 10);
    g.setFont(font);
    g.setColor(Color.BLUE);
    g.drawString(octopus.getName(), 0, avatar.getHeight());
    return avatar;
  }
}
