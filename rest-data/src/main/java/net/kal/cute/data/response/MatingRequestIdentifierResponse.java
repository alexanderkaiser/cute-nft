package net.kal.cute.data.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.kal.cute.data.MatingRequestStatus;

@Data
@NoArgsConstructor
public class MatingRequestIdentifierResponse {

  private String requestId;
  private MatingRequestStatus status;
}
