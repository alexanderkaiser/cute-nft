package net.kal.cute.data.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserAccountData {

  private String publicKey;
  private String userName;
  private Double balance;
}
