package net.kal.cute.client.rest;

import io.restassured.common.mapper.TypeRef;
import io.restassured.specification.RequestSpecification;
import java.security.KeyPair;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.val;
import net.kal.cute.CuteRequest;
import net.kal.cute.data.response.TransactionEnvelope;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GetAllTransactions implements CuteRequest<List<TransactionEnvelope>> {

  public static GetAllTransactions create() {
    return new GetAllTransactions();
  }

  @Override
  public List<TransactionEnvelope> perform(RequestSpecification reqSpec, KeyPair keyPair) {
    val type = new TypeRef<List<TransactionEnvelope>>() {};
    return reqSpec.basePath("/api/cute/transactions").get().getBody().as(type);
  }
}
