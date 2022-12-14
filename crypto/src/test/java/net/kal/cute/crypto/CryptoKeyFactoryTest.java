package net.kal.cute.crypto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.crypto.Cipher;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

class CryptoKeyFactoryTest {

  @Test
  void shouldGenerateRandomKeyPair() {
    val kp = CryptoKeyFactory.generateRandomKeyPair();
    assertNotNull(kp);
    assertNotNull(kp.getPrivate());
    assertNotNull(kp.getPublic());
    assertEquals("RSA", kp.getPublic().getAlgorithm());
    assertEquals("RSA", kp.getPrivate().getAlgorithm());
  }

  @Test
  void shouldRestorePublicKeyFromEncoded() {
    val kp = CryptoKeyFactory.generateRandomKeyPair();
    val pubKeyEncoded = kp.getPublic().getEncoded();
    val pubKey = CryptoKeyFactory.restorePublicKey(pubKeyEncoded);
    assertEquals(kp.getPublic(), pubKey);
  }

  @Test
  void shouldRestorePublicKeyFromString() {
    val kp = CryptoKeyFactory.generateRandomKeyPair();
    val pubKeyString = CryptoKeyFactory.publicKeyToString(kp.getPublic());
    val pubKey = CryptoKeyFactory.restorePublicKey(pubKeyString);
    assertEquals(kp.getPublic(), pubKey);
  }

  @SneakyThrows
  @Test
  void shouldEncryptWithKeyPair() {
    val sourceMessage = "Hello World";
    val kp = CryptoKeyFactory.generateRandomKeyPair();

    val encCipher = Cipher.getInstance(kp.getPrivate().getAlgorithm());
    encCipher.init(Cipher.ENCRYPT_MODE, kp.getPrivate());

    val encrypted = encCipher.doFinal(sourceMessage.getBytes());

    val decCipher = Cipher.getInstance(kp.getPublic().getAlgorithm());
    decCipher.init(Cipher.DECRYPT_MODE, kp.getPublic());

    val decrypted = new String(decCipher.doFinal(encrypted));

    assertEquals(sourceMessage, decrypted);
  }
}
