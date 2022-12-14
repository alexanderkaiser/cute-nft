package net.kal.cute.server.ctx;

import static java.text.MessageFormat.format;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.val;
import net.kal.cute.data.response.UserAccountData;

public class UserService {

  @Getter
  private final Map<String, UserAccountData> users;

  public UserService() {
    this.users = new HashMap<>();
  }

  public boolean hasUser(String publicKey) {
  return getUser(publicKey).isPresent();
  }

  public Optional<UserAccountData> getUser(String publicKey) {
    return Optional.ofNullable(users.get(publicKey));
  }

  public void changeBalance(String publicKey, double balanceDiff) {
    users.forEach((k, v) -> {
      if (k.equals(publicKey)) {
        v.setBalance(v.getBalance() + balanceDiff);
      }
    });
  }

  public UserAccountData createUser(String publicKey, String userName, double initialBalance) {
    val userAccountData = new UserAccountData();
    userAccountData.setPublicKey(publicKey);
    userAccountData.setUserName(userName);
    userAccountData.setBalance(initialBalance);
    users.put(publicKey, userAccountData);
    return userAccountData;
  }
}
