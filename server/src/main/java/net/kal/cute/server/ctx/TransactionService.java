package net.kal.cute.server.ctx;

import static java.text.MessageFormat.format;

import java.security.KeyPair;
import java.util.LinkedList;
import java.util.List;
import lombok.Getter;
import lombok.val;
import net.kal.cute.data.response.UserAccountData;
import net.kal.cute.octopus.model.Octopus;
import net.kal.cute.data.response.TransactionEnvelope;

public class TransactionService {

  private final KeyPair masterKeys;
  private final OctopusService octopusService;
  private final UserService userService;
  @Getter private final List<TransactionEnvelope> transactions;

  public TransactionService(KeyPair masterKeys, OctopusService octopusService, UserService userService) {
    this.transactions = new LinkedList<>();
    this.masterKeys = masterKeys;
    this.octopusService = octopusService;
    this.userService = userService;
  }

  public long perform(UserAccountData seller, UserAccountData buyer, Octopus octopus, double price) {
    val tb =
        TransactionBuilder.from(seller)
            .sellingTo(buyer)
            .theOctopus(octopus)
            .forPrice(price)
            .authorizedBy(masterKeys.getPrivate());

    if (!octopusService.changeOwnership(octopus, seller, buyer)) {
      // TODO: fix later
      throw new RuntimeException(format("you cannot sell octopuses you don't own!"));
    }

    // TODO: check balances later
    userService.changeBalance(seller.getPublicKey(), price);
    userService.changeBalance(buyer.getPublicKey(), -price);

    this.transactions.add(tb);
    return tb.getIdentifier();
  }
}
