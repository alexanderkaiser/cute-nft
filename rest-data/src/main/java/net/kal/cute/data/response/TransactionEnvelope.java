package net.kal.cute.data.response;

import java.beans.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEnvelope {

  private OctopusTransaction transaction;
  private String signature;

  @Transient
  public long getIdentifier() {
    return transaction.getTimestamp();
  }
}
