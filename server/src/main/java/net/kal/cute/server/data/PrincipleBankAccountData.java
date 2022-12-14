package net.kal.cute.server.data;

import lombok.Data;
import net.kal.cute.data.response.UserAccountData;

@Data
public class PrincipleBankAccountData extends UserAccountData {

  public PrincipleBankAccountData() {
    super();
    this.setBalance(Double.MAX_VALUE);
  }

  public void setBalance() {
    // nothing to do, we won't ever change the balance of principle bank account
  }
}
