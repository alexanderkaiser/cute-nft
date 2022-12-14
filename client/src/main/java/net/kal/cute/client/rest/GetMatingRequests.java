package net.kal.cute.client.rest;

import io.restassured.common.mapper.TypeRef;
import io.restassured.specification.RequestSpecification;
import java.security.KeyPair;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.val;
import net.kal.cute.CuteRequest;
import net.kal.cute.data.request.MatingRequestData;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GetMatingRequests implements CuteRequest<List<MatingRequestData>> {

  public static GetMatingRequests forUser() {
    return new GetMatingRequests();
  }

  @Override
  public List<MatingRequestData> perform(RequestSpecification reqSpec, KeyPair keyPair) {
    val type = new TypeRef<List<MatingRequestData>>() {};
    return reqSpec
        .basePath("/api/account/mating/requests")
        .header("pub-key", publicKey(keyPair))
        .get()
        .getBody()
        .as(type);
  }
}
