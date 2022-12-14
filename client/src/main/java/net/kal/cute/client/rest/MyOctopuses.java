package net.kal.cute.client.rest;

import io.restassured.common.mapper.TypeRef;
import io.restassured.specification.RequestSpecification;
import java.security.KeyPair;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.val;
import net.kal.cute.CuteRequest;
import net.kal.cute.octopus.model.Octopus;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MyOctopuses implements CuteRequest<List<Octopus>> {

  public static MyOctopuses fromInventory() {
    return new MyOctopuses();
  }

  @Override
  public List<Octopus> perform(RequestSpecification reqSpec, KeyPair keyPair) {
    val type = new TypeRef<List<Octopus>>() {};
    return reqSpec
        .basePath("/api/account/inventory")
        .header("pub-key", publicKey(keyPair))
        .get()
        .getBody()
        .as(type);
  }
}
