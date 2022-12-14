package net.kal.cute.crypto;

import static java.text.MessageFormat.format;

import lombok.val;

public class Main {

  public static void main(String[] args) {
    val kp = CryptoKeyFactory.generateRandomKeyPair();
    System.out.println(format("Public Key: {0}", CryptoKeyFactory.publicKeyToString(kp.getPublic())));
  }
}
