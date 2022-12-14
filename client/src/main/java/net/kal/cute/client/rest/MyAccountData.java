package net.kal.cute.client.rest;

import io.restassured.specification.RequestSpecification;
import java.security.KeyPair;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.kal.cute.CuteRequest;
import net.kal.cute.data.response.UserAccountData;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MyAccountData implements CuteRequest<UserAccountData>  {

  public static MyAccountData fromServer() {
    return new MyAccountData();
  }

  @Override
  public UserAccountData perform(RequestSpecification reqSpec, KeyPair keyPair) {
    return reqSpec
        .basePath("/api/account")
        .header("pub-key", publicKey(keyPair))
        .get()
        .getBody()
        .as(UserAccountData.class);
  }
}
