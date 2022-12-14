package net.kal.cute.client.rest;

import io.restassured.common.mapper.TypeRef;
import io.restassured.specification.RequestSpecification;
import java.security.KeyPair;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.val;
import net.kal.cute.CuteRequest;
import net.kal.cute.data.response.OctopusOwnership;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GetAllOctopuses implements CuteRequest<List<OctopusOwnership>> {

  public static GetAllOctopuses create() {
    return new GetAllOctopuses();
  }

  @Override
  public List<OctopusOwnership> perform(RequestSpecification reqSpec, KeyPair keyPair) {
    val type = new TypeRef<List<OctopusOwnership>>() {};
    return reqSpec.basePath("/api/cute/octopuses").get().getBody().as(type);
  }
}
