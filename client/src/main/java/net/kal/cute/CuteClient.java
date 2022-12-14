package net.kal.cute;

public interface CuteClient {

  <R> R request(CuteRequest<R> request);
}
