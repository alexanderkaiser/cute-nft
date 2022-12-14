package net.kal.cute.client.rest;

import io.restassured.mapper.ObjectMapperType;
import io.restassured.specification.RequestSpecification;
import java.security.KeyPair;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.val;
import net.kal.cute.CuteRequest;
import net.kal.cute.data.request.UserRegistrationData;
import net.kal.cute.data.response.UserAccountData;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Registration implements CuteRequest<UserAccountData> {

  private final String username;

  public static Registration withUsername(String username) {
    return new Registration(username);
  }

  public static Registration withRandomUsername() {
    return new Registration(null);
  }

  @Override
  public UserAccountData perform(RequestSpecification reqSpec, KeyPair keyPair) {
    val data = new UserRegistrationData();
    data.setUserName(this.username);

    return reqSpec
        .basePath("/api/account/register")
        .header("pub-key", publicKey(keyPair))
        .body(data, ObjectMapperType.JACKSON_2)
        .post()
        .getBody()
        .as(UserAccountData.class, ObjectMapperType.JACKSON_2);
  }
}
