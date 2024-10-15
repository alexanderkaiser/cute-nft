package net.kal.cute.octopus;

import static java.text.MessageFormat.format;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import lombok.val;
import net.kal.cute.octopus.model.OctopusFactory;

public class Main {

  public static void main(String[] args) throws IOException {
    val factory = new OctopusFactory();

    for (var i = 0; i < 20; i++) {
      val g1 = factory.createRandomOctopus();
      val g2 = factory.createRandomOctopus();
      if (factory.allowedToMate(g1, g2)) {
        val offspring = factory.mate(g1, g2);

        System.out.println("First:     " + g1);
        System.out.println("Second:    " + g2);
        System.out.println("Offspring: " + offspring);

        val img = factory.createAvatar(offspring);
        val outputfile = new File(format("offspring_{0}.png", i));
        System.out.println(outputfile.getAbsolutePath());
        ImageIO.write(img, "png", outputfile);

        System.out.println("----------------------------");
      } else {
        System.out.println(format("{0} and {1} are not allowed to mate!", g1, g2));
      }
    }
  }
}
