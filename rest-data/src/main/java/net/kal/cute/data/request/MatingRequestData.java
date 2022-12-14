package net.kal.cute.data.request;

import java.beans.Transient;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Data
public class MatingRequestData {

  private String materId;
  private String partnerId;
  private double reward;
  private String requesterSignature;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MatingRequestData that = (MatingRequestData) o;

    return new EqualsBuilder().append(reward, that.reward)
        .append(materId, that.materId).append(partnerId, that.partnerId)
        .append(requesterSignature, that.requesterSignature).isEquals();
  }

  @Transient
  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(materId).append(partnerId).append(reward)
        .append(requesterSignature).toHashCode();
  }
}
