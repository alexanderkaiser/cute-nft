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
import net.kal.cute.data.MatingRequestResponse;
import net.kal.cute.data.request.MatingRequestAnswerData;
import net.kal.cute.data.response.MatingRequestIdentifierResponse;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AnswerMatingRequest implements CuteRequest<MatingRequestIdentifierResponse> {

  private final MatingRequestAnswerData answerData;

  @SneakyThrows
  @Override
  public MatingRequestIdentifierResponse perform(RequestSpecification reqSpec, KeyPair keyPair) {
    val privateSignature = Signature.getInstance("SHA256withRSA");
    privateSignature.initSign(keyPair.getPrivate());
    privateSignature.update(answerData.getRequestId().getBytes(StandardCharsets.UTF_8));
    val signature = privateSignature.sign();
    answerData.setRequesterSignature(Base64.getEncoder().encodeToString(signature));

    return reqSpec
        .basePath("/api/mating/answer")
        .header("pub-key", publicKey(keyPair))
        .body(answerData, ObjectMapperType.JACKSON_2)
        .post()
        .getBody()
        .as(MatingRequestIdentifierResponse.class);
  }

  public static Builder accept() {
    return new Builder(MatingRequestResponse.ACCEPT);
  }

  public static Builder decline() {
    return new Builder(MatingRequestResponse.DECLINE);
  }

  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Builder {
    private final MatingRequestResponse matingResponse;

    public AnswerMatingRequest request(MatingRequestIdentifierResponse request) {
      return request(request.getRequestId());
    }

    public AnswerMatingRequest request(String id) {
      val matingAnswer = new MatingRequestAnswerData();
      matingAnswer.setRequestId(id);
      matingAnswer.setResponse(matingResponse);
      return new AnswerMatingRequest(matingAnswer);
    }
  }
}
