package net.kal.cute.data.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.kal.cute.octopus.model.Octopus;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OctopusTransaction {

  private String seller;
  private String sellerName;
  private String buyer;
  private String buyerName;
  private Octopus octopus;
  private double price;
  private long timestamp;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    OctopusTransaction that = (OctopusTransaction) o;

    return new EqualsBuilder().append(price, that.price)
        .append(timestamp, that.timestamp).append(seller, that.seller).append(buyer, that.buyer)
        .append(octopus, that.octopus).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(seller).append(buyer).append(octopus).append(price)
        .append(timestamp).toHashCode();
  }
}
