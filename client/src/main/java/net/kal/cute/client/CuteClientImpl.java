package net.kal.cute.client;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import java.security.KeyPair;
import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.val;
import net.kal.cute.CuteClient;
import net.kal.cute.CuteRequest;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CuteClientImpl implements CuteClient {

  private final String baseUri;
  private final KeyPair keyPair;

  private final Supplier<RequestSpecification> requestSpecificationSupplier;

  public static Builder forUrl(String url) {
    return new Builder(url);
  }

  @Override
  public <R> R request(CuteRequest<R> request) {
    val reqSpec =
        requestSpecificationSupplier
            .get()
            .baseUri(baseUri)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON);
    return request.perform(reqSpec, keyPair);
  }

  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Builder {
    private final String baseUrl;
    private KeyPair keyPair;

    public Builder with(KeyPair keyPair) {
      this.keyPair = keyPair;
      return this;
    }

    public CuteClientImpl andDefaultSpec() {
      return andCustomSpec(RestAssured::given);
    }

    public CuteClientImpl andCustomSpec(
        Supplier<RequestSpecification> requestSpecificationSupplier) {
      return new CuteClientImpl(baseUrl, keyPair, requestSpecificationSupplier);
    }
  }
}
