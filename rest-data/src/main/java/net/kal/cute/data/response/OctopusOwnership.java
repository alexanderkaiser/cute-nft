package net.kal.cute.data.response;

import java.util.LinkedList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;
import net.kal.cute.octopus.model.Octopus;

@Data
@NoArgsConstructor
public class OctopusOwnership {

  private String publicKey;
  private String userName;
  private List<Octopus> octopuses = new LinkedList<>();

  public static OctopusOwnership forUser(String publicKey, String userName) {
    val oos = new OctopusOwnership();
    oos.setPublicKey(publicKey);
    oos.setUserName(userName);
    return oos;
  }
}
