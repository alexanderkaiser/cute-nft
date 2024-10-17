package net.kal.cute.bdd;

import lombok.val;
import net.kal.cute.CuteClient;
import net.kal.cute.client.CuteClientImpl;
import net.kal.cute.crypto.CryptoKeyFactory;
import net.serenitybdd.rest.SerenityRest;

public class TestcaseData {

  public String getUrl() {
    return "http://localhost:8080";
  }

  public CuteClient createClient() {
    return CuteClientImpl.forUrl(this.getUrl())
        .with(CryptoKeyFactory.generateRandomKeyPair())
//        .andDefaultSpec();
        .andCustomSpec(SerenityRest::given);
  }

  public CuteClient createClientFor(String name) {
    val builder = CuteClientImpl.forUrl(this.getUrl())
        .with(CryptoKeyFactory.generateRandomKeyPair());

    if (name.equals("Alice")) {
      return builder.andCustomSpec(SerenityRest::given);
    } else {
      return builder.andDefaultSpec();
    }
  }
}
