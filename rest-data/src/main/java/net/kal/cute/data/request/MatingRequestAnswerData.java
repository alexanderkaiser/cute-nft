package net.kal.cute.data.request;

import lombok.Data;
import net.kal.cute.data.MatingRequestResponse;

@Data
public class MatingRequestAnswerData {

  private MatingRequestResponse response;
  private String requestId;
  private String requesterSignature;
}
