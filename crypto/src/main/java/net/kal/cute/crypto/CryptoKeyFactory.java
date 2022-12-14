package net.kal.cute.crypto;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import lombok.SneakyThrows;
import lombok.val;

public class CryptoKeyFactory {

  @SneakyThrows
  public static KeyPair generateRandomKeyPair() {
    val rnd = new SecureRandom();
    val generator = KeyPairGenerator.getInstance("RSA");
    generator.initialize(2048, rnd);

    return generator.generateKeyPair();
  }

  public static String publicKeyToString(PublicKey publicKey) {
    return new String(Base64.getEncoder().encode(publicKey.getEncoded()));
  }

  public static PublicKey restorePublicKey(String key) {
    val keyBytes = Base64.getDecoder().decode(key.getBytes());
    return restorePublicKey(keyBytes);
  }

  @SneakyThrows
  public static PublicKey restorePublicKey(byte[] key) {
    val x509publicKey = new X509EncodedKeySpec(key);
    val kf = KeyFactory.getInstance("RSA");
    return kf.generatePublic(x509publicKey);
  }
}
