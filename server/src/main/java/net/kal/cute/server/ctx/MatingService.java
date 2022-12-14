package net.kal.cute.server.ctx;

import static java.text.MessageFormat.format;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import java.security.Signature;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import lombok.SneakyThrows;
import lombok.val;
import net.kal.cute.crypto.CryptoKeyFactory;
import net.kal.cute.data.MatingRequestResponse;
import net.kal.cute.data.MatingRequestStatus;
import net.kal.cute.data.error.MessageError;
import net.kal.cute.data.request.MatingRequestAnswerData;
import net.kal.cute.data.request.MatingRequestData;
import net.kal.cute.data.response.MatingRequestIdentifierResponse;
import net.kal.cute.data.response.OctopusOwnership;
import net.kal.cute.octopus.model.Octopus;
import net.kal.cute.octopus.utils.UUIDHelper;

public class MatingService {

  private final Map<String, MatingRequestData> requests;

  private final UserService userService;
  private final OctopusService octopusService;
  private final TransactionService transactionService;

  public MatingService(UserService userService, OctopusService octopusService, TransactionService transactionService) {
    this.requests = new HashMap<>();
    this.userService = userService;
    this.octopusService = octopusService;
    this.transactionService = transactionService;
  }

  @SneakyThrows
  public MatingRequestIdentifierResponse makeRequest(
      String pubKey, MatingRequestData matingRequest) {
    val requester =
        userService
            .getUser(pubKey)
            .orElseThrow(
                () ->
                    new WebApplicationException(
                        Response.status(403)
                            .entity(
                                MessageError.withMessage(
                                    format(
                                        "You are not yet registered with public key {0}", pubKey)))
                            .build()));

    val mater =
        octopusService
            .getOctopusById(matingRequest.getMaterId())
            .orElseThrow(
                () ->
                    new WebApplicationException(
                        Response.status(404)
                            .entity(
                                MessageError.withMessage(
                                    format(
                                        "Octopus with id {0} not found",
                                        matingRequest.getMaterId())))
                            .build()));

    if (!octopusService.isOwnedBy(mater, requester)) {
      throw new WebApplicationException(
          Response.status(403)
              .entity(
                  MessageError.withMessage(
                      format(
                          "You can only request mating for octopuses you own but you don't have ownership for {0}",
                          mater.getIdentifier())))
              .build());
    }

    val partner =
        octopusService
            .getOctopusById(matingRequest.getPartnerId())
            .orElseThrow(
                () ->
                    new WebApplicationException(
                        Response.status(404)
                            .entity(
                                MessageError.withMessage(
                                    format(
                                        "Octopus with id {0} not found",
                                        matingRequest.getPartnerId())))
                            .build()));

    if (octopusService.isOwnedBy(partner, requester)) {
      throw new WebApplicationException(
          Response.status(403)
              .entity(
                  MessageError.withMessage(
                      format(
                          "You cannot request mating for your own octopuses but you have ownership for {0}",
                          partner.getIdentifier())))
              .build());
    }

    // check the signature
    val publicKey = CryptoKeyFactory.restorePublicKey(requester.getPublicKey());
    val sig = Signature.getInstance("SHA256withRSA");
    sig.initVerify(publicKey);
    sig.update(matingRequest.getMaterId().getBytes(StandardCharsets.UTF_8));
    val signatureBytes =
        Base64.getDecoder()
            .decode(matingRequest.getRequesterSignature().getBytes(StandardCharsets.UTF_8));
    if (!sig.verify(signatureBytes)) {
      throw new WebApplicationException(
          Response.status(403)
              .entity(
                  MessageError.withMessage(
                      format(
                          "You are not authorized to request mating for {0}",
                          mater.getIdentifier())))
              .build());
    }

    val id = UUIDHelper.uuidStringFromUuid16(matingRequest.hashCode());
    val resp = new MatingRequestIdentifierResponse();
    resp.setRequestId(id);
    resp.setStatus(MatingRequestStatus.OPEN);
    this.requests.put(id, matingRequest);
    return resp;
  }

  @SneakyThrows
  public MatingRequestIdentifierResponse responseRequest(
      String pubKey, MatingRequestAnswerData answerData) {
    //    val matingRequest = this.requests.get(answerData.getRequestId());
    val matingRequest = this.requests.remove(answerData.getRequestId());

    val answerer =
        userService
            .getUser(pubKey)
            .orElseThrow(
                () ->
                    new WebApplicationException(
                        Response.status(403)
                            .entity(
                                MessageError.withMessage(
                                    format(
                                        "You are not yet registered with public key {0}", pubKey)))
                            .build()));

    // check the signature
    val publicKey = CryptoKeyFactory.restorePublicKey(answerer.getPublicKey());
    val sig = Signature.getInstance("SHA256withRSA");
    sig.initVerify(publicKey);
    sig.update(answerData.getRequestId().getBytes(StandardCharsets.UTF_8));
    val signatureBytes =
        Base64.getDecoder()
            .decode(answerData.getRequesterSignature().getBytes(StandardCharsets.UTF_8));
    if (!sig.verify(signatureBytes)) {
      throw new WebApplicationException(
          Response.status(403)
              .entity(
                  MessageError.withMessage(
                      format(
                          "You are not authorized to answer mating request {0}",
                          answerData.getRequestId())))
              .build());
    }

    val mater =
        octopusService
            .getOctopusById(matingRequest.getMaterId())
            .orElseThrow(
                () ->
                    new WebApplicationException(
                        Response.status(404)
                            .entity(
                                MessageError.withMessage(
                                    format(
                                        "Mater Octopus with ID {0} not found",
                                        matingRequest.getMaterId())))
                            .build()));

    Supplier<WebApplicationException> requesterNotFound =
        () ->
            new WebApplicationException(
                Response.status(404)
                    .entity(
                        MessageError.withMessage(
                            format(
                                "Owner of the mater octopus with {0} could not be found",
                                mater.getIdentifier())))
                    .build());
    val requester =
        octopusService.getOctopusOwnerships().stream()
            .filter(oos -> oos.getOctopuses().contains(mater))
            .map(OctopusOwnership::getPublicKey)
            .map(requesterPubKey -> userService.getUser(requesterPubKey).orElseThrow(requesterNotFound))
            .findFirst()
            .orElseThrow(requesterNotFound);

    val partner =
        octopusService
            .getOctopusById(matingRequest.getPartnerId())
            .orElseThrow(
                () ->
                    new WebApplicationException(
                        Response.status(404)
                            .entity(
                                MessageError.withMessage(
                                    format(
                                        "Partner Octopus with ID {0} not found",
                                        matingRequest.getPartnerId())))
                            .build()));

    val resp = new MatingRequestIdentifierResponse();
    resp.setRequestId(answerData.getRequestId());

    if (answerData.getResponse() == MatingRequestResponse.ACCEPT) {
      resp.setStatus(MatingRequestStatus.ACCEPTED);
      // first the offspring goes to the answerer
      val offspring = octopusService.mate(mater, partner, answerer);
      // second the answerer sells the offspring to the requester for the agreed reward
      transactionService.perform(answerer, requester, offspring, matingRequest.getReward());
    } else {
      resp.setStatus(MatingRequestStatus.DECLINED);
    }

    return resp;
  }

  public List<MatingRequestData> getRequestsForUser(String pubKey) {
    val octos = octopusService.getInventory(pubKey).stream().map(Octopus::getIdentifier).toList();
    return this.requests.values().stream().filter(r -> octos.contains(r.getPartnerId())).toList();
  }
}
