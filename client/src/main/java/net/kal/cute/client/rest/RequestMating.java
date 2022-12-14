package net.kal.cute.client.rest;

import io.restassured.mapper.ObjectMapperType;
import io.restassured.specification.RequestSpecification;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.Signature;
import java.util.Base64;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import net.kal.cute.CuteRequest;
import net.kal.cute.data.request.MatingRequestData;
import net.kal.cute.data.response.MatingRequestIdentifierResponse;
import net.kal.cute.octopus.model.Octopus;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestMating implements CuteRequest<MatingRequestIdentifierResponse> {

  private final MatingRequestData requestData;

  public static Builder forOctopus(Octopus mater) {
    return forOctopus(mater.getIdentifier());
  }

  public static Builder forOctopus(String mater) {
    return new Builder(mater);
  }

  @SneakyThrows
  @Override
  public MatingRequestIdentifierResponse perform(RequestSpecification reqSpec, KeyPair keyPair) {
    val privateSignature = Signature.getInstance("SHA256withRSA");
    privateSignature.initSign(keyPair.getPrivate());
    privateSignature.update(requestData.getMaterId().getBytes(StandardCharsets.UTF_8));
    val signature = privateSignature.sign();
    requestData.setRequesterSignature(Base64.getEncoder().encodeToString(signature));

    return reqSpec
        .basePath("/api/mating/request")
        .header("pub-key", publicKey(keyPair))
        .body(requestData, ObjectMapperType.JACKSON_2)
        .post()
        .getBody()
        .as(MatingRequestIdentifierResponse.class);
  }

  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Builder {
    private final String mater;
    private String partner;

    public Builder toMateWith(Octopus partner) {
      return toMateWith(partner.getIdentifier());
    }

    public Builder toMateWith(String partner) {
      this.partner = partner;
      return this;
    }

    public RequestMating forReward(double reward) {
      val matingData = new MatingRequestData();
      matingData.setMaterId(mater);
      matingData.setPartnerId(partner);
      matingData.setReward(reward);
      return new RequestMating(matingData);
    }
  }
}
