package net.kal.cute.data.error;

import lombok.Data;
import lombok.val;

@Data
public class MessageError {

  private String message;

  public static MessageError withMessage(String message) {
    val e = new MessageError();
    e.setMessage(message);
    return e;
  }
}
