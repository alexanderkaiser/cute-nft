package net.kal.cute;

import io.restassured.specification.RequestSpecification;
import java.security.KeyPair;
import net.kal.cute.crypto.CryptoKeyFactory;

public interface CuteRequest<T> {

  T perform(RequestSpecification reqSpec, KeyPair keyPair);

  default String publicKey(KeyPair keyPair) {
    return CryptoKeyFactory.publicKeyToString(keyPair.getPublic());
  }
}
