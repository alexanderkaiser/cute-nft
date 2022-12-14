package net.kal.cute.octopus.model;

import static java.text.MessageFormat.format;

import java.beans.Transient;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import net.kal.cute.octopus.utils.UUIDHelper;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Data
@NoArgsConstructor
public class Octopus {

  private String identifier;
  private String name;
  private Long genome;
  private Gender gender;
  private Double fitness;

  public Octopus(String name, Long genome) {
    this.name = name;
    this.genome = genome;
    this.gender = (genome < 0) ? Gender.FEMALE : Gender.MALE;

    val abs = genome & Long.MAX_VALUE; // gender neutral value!
    this.fitness =
        abs * 100.0 / Long.MAX_VALUE; // rule of three where Long.MAX_VALUE equals to 100% fitness!

    this.identifier = UUIDHelper.uuidStringFromUuid16(this.hashCode());
  }

  /**
   * The raw value is always an absolute value (unsigned). The LSB represents the gender and is
   * masked out by intention. Use the {@link #getGender()} method to retrieve the LSB
   *
   * @return the raw genome without the gender-bit
   */
  public Long getGenome() {
    return genome & Long.MAX_VALUE;
  }

  @Transient
  public String getGenomeString() {
    StringBuilder bin = new StringBuilder(Long.toBinaryString(genome));
    while (bin.length() < 64) {
      bin.insert(0, "0"); // ensure that always 64 bits for positive numbers
    }
    return bin.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Octopus octopus = (Octopus) o;

    return new EqualsBuilder().append(name, octopus.name).append(genome, octopus.genome).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(name).append(genome).toHashCode();
  }

  @Override
  public String toString() {
    return format("{0} ({1})", name, getGender());
  }
}
