package net.kal.cute.server.ctx;

import java.nio.ByteBuffer;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.Date;
import javax.crypto.Cipher;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import net.kal.cute.data.response.UserAccountData;
import net.kal.cute.octopus.model.Octopus;
import net.kal.cute.data.response.OctopusTransaction;
import net.kal.cute.data.response.TransactionEnvelope;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionBuilder {
  private final String seller;
  private final String sellerName;
  private String buyer;
  private String buyerName;
  private Octopus octopus;
  private double price;

  public static TransactionBuilder from(UserAccountData seller) {
    return from(seller.getPublicKey(), seller.getUserName());
  }
  public static TransactionBuilder from(String seller, String sellerName) {
    return new TransactionBuilder(seller, sellerName);
  }

  public TransactionBuilder sellingTo(UserAccountData buyer) {
    this.buyer = buyer.getPublicKey();
    this.buyerName = buyer.getUserName();
    return this;
  }

  public TransactionBuilder theOctopus(Octopus octopus) {
    this.octopus = octopus;
    return this;
  }

  public TransactionBuilder forPrice(double price) {
    this.price = price;
    return this;
  }

  @SneakyThrows
  public TransactionEnvelope authorizedBy(PrivateKey privateKey) {
    val transaction = new OctopusTransaction(seller, sellerName, buyer, buyerName, octopus, price, new Date().getTime());
    val cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, privateKey);
    val transactionHash = ByteBuffer.allocate(8).putInt(transaction.hashCode()).array();
    val signature = cipher.doFinal(transactionHash);
    val signatureB64 = Base64.getEncoder().encodeToString(signature);
    return new TransactionEnvelope(transaction, signatureB64);
  }
}
