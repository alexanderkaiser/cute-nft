package net.kal.cute;

import static java.text.MessageFormat.format;

import java.util.List;
import lombok.val;
import net.kal.cute.client.CuteClientImpl;
import net.kal.cute.client.rest.AnswerMatingRequest;
import net.kal.cute.client.rest.GetAllOctopuses;
import net.kal.cute.client.rest.GetAllTransactions;
import net.kal.cute.client.rest.GetMatingRequests;
import net.kal.cute.client.rest.MyAccountData;
import net.kal.cute.client.rest.MyOctopuses;
import net.kal.cute.client.rest.Registration;
import net.kal.cute.client.rest.RequestMating;
import net.kal.cute.crypto.CryptoKeyFactory;
import net.kal.cute.data.response.UserAccountData;
import net.kal.cute.octopus.model.Octopus;

public class Main {

  public static void main(String[] args) {
    val client1 =
        CuteClientImpl.forUrl("http://localhost:8080")
            .with(CryptoKeyFactory.generateRandomKeyPair())
            .andDefaultSpec();

    val client2 =
        CuteClientImpl.forUrl("http://localhost:8080")
            .with(CryptoKeyFactory.generateRandomKeyPair())
            .andDefaultSpec();

    val accountData1 = registerUser(client1);
    val accountData2 = registerUser(client2);

    // show me all transactions on server
    // showAllTransactions(client1);

    // show me all living octopuses on server
    // showAllOctopuses(client2);

    System.out.println("<========== Inventory before mating ==========>");
    val inventory1 = getInventory(client1);
    val inventory2 = getInventory(client2);
    System.out.println("X========== Inventory before mating ==========X\n");

    // request mating from user1 to user2
    val mater = inventory1.get(0);
    val partner = inventory2.get(1);
    System.out.println(format("<========== Mating Request: {0} x {1} ==========>", mater, partner));
    val matingRequest =
        client1.request(
            RequestMating.forOctopus(mater)
                .toMateWith(partner)  // initially the second octopus is always male
                .forReward(123.5));
    System.out.println(format("MatingRequest: {0}", matingRequest));

    val receivedRequests = client2.request(GetMatingRequests.forUser());
    System.out.println(format("Received Mating Requests: {0}", receivedRequests.size()));
    receivedRequests.forEach(r -> System.out.println(format("\t{0}", r)));
    System.out.println(
        format("X========== Mating Request: {0} x {1} ==========X\n", mater, partner));

    // answer the response by accepting
    System.out.println(format("<========== Answer Mating Request: {0} ==========>", matingRequest.getRequestId()));
    val matingAnswer =
        client2.request(AnswerMatingRequest.accept().request(matingRequest.getRequestId()));
//    val matingAnswer =
//        client2.request(AnswerMatingRequest.decline().request(matingRequest.getRequestId()));
    System.out.println(format("MatingAnswer: {0}", matingAnswer));
    System.out.println(format("X========== Answer Mating Request: {0} ==========X\n", matingRequest.getRequestId()));

    System.out.println("<========== Inventory after mating ==========>");
    val inventory12 = getInventory(client1);
    val inventory22 = getInventory(client2);
    System.out.println("X========== Inventory after mating ==========X\n");

    System.out.println("<========== Transactions after mating ==========>");
    showAllTransactions(client1);
    System.out.println("X========== Transactions after mating ==========X\n");
  }

  private static UserAccountData registerUser(CuteClient client) {
    val response = client.request(Registration.withRandomUsername());
    System.out.println(
        format(
            "Registered user {0} with initial balance {1}",
            response.getUserName(), response.getBalance()));
    return response;
  }

  private static List<Octopus> getInventory(CuteClient client) {
    val accountData = client.request(MyAccountData.fromServer());
    val octopuses = client.request(MyOctopuses.fromInventory());
    System.out.println(
        format("The user ''{0}'' has {1} octopuses and {2} coins", accountData.getUserName(), octopuses.size(), accountData.getBalance()));
    octopuses.forEach(
        octo -> System.out.println(format("\t{0} with ID {1}", octo, octo.getIdentifier())));
    return octopuses;
  }

  private static void showAllTransactions(CuteClient client) {
    val response = client.request(GetAllTransactions.create());
    System.out.println(format("Found {0} executed transactions on server", response.size()));
    response.forEach(
        t ->
            System.out.println(
                format(
                    "\t$> {0} sold {1} to {2} for {3}",
                    t.getTransaction().getSellerName(),
                    t.getTransaction().getOctopus(),
                    t.getTransaction().getBuyerName(),
                    t.getTransaction().getPrice())));
  }

  private static void showAllOctopuses(CuteClient client) {
    val response = client.request(GetAllOctopuses.create());
    System.out.println(format("Found {0} living octopuses on server", response.size()));
    response.forEach(
        oos -> {
          System.out.println(
              format("{0} has {1} Octopuses", oos.getUserName(), oos.getOctopuses().size()));
          oos.getOctopuses()
              .forEach(
                  octo ->
                      System.out.println(format("\t{0} with ID {1}", octo, octo.getIdentifier())));
        });
  }
}
