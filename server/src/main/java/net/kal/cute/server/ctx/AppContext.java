package net.kal.cute.server.ctx;

import com.github.javafaker.Faker;
import jakarta.inject.Singleton;
import java.security.KeyPair;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Getter;
import lombok.val;
import net.kal.cute.crypto.CryptoKeyFactory;
import net.kal.cute.data.request.UserRegistrationData;
import net.kal.cute.data.response.UserAccountData;
import net.kal.cute.octopus.model.Gender;
import net.kal.cute.server.data.PrincipleBankAccountData;

@Singleton
public class AppContext {

  private final KeyPair keyPair;
  private final PrincipleBankAccountData principleBank;

  @Getter private final OctopusService octopusService;
  @Getter private final UserService userService;
  @Getter private final TransactionService transactionService;
  @Getter private final MatingService matingService;

  public AppContext() {
    this.keyPair = CryptoKeyFactory.generateRandomKeyPair();
    this.principleBank = new PrincipleBankAccountData();
    this.principleBank.setPublicKey(CryptoKeyFactory.publicKeyToString(this.keyPair.getPublic()));
    this.principleBank.setUserName("CP-Bank");  // Cute Principle Bank

    this.userService = new UserService();
    this.userService.getUsers().put(this.principleBank.getPublicKey(), this.principleBank);

    this.octopusService = new OctopusService();
    this.transactionService = new TransactionService(this.keyPair, octopusService, userService);
    this.matingService = new MatingService(userService, octopusService, transactionService);
  }

  public UserAccountData registerNewUser(String publicKey, UserRegistrationData registrationData) {
    if (userService.hasUser(publicKey)) {
      return userService.getUser(publicKey).orElseThrow();
    }

    val newUser = userService.createUser(publicKey, registrationData.getUserName(), 400.0);

    val first = octopusService.createFor(principleBank, Gender.FEMALE);
    val second = octopusService.createFor(principleBank, Gender.MALE);

    List.of(first, second)
        .forEach(octo -> transactionService.perform(principleBank, newUser, octo, 20.0)); // intended bug!

    return newUser;
  }
}
