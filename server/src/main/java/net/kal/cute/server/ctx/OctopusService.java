package net.kal.cute.server.ctx;

import static java.text.MessageFormat.format;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.kal.cute.data.response.OctopusOwnership;
import net.kal.cute.data.response.UserAccountData;
import net.kal.cute.octopus.model.Gender;
import net.kal.cute.octopus.model.Octopus;
import net.kal.cute.octopus.model.OctopusFactory;

@Slf4j
public class OctopusService {

  private final OctopusFactory octopusFactory;

  @Getter private final Map<String, OctopusOwnership> octopusOwnerships;

  public OctopusService() {
    this.octopusFactory = new OctopusFactory();
    this.octopusOwnerships = new HashMap<>();
  }

  public Octopus createFor(UserAccountData owner, Gender gender) {
    val octo = octopusFactory.createRandomOctopus(gender);
    this.octopusOwnerships
        .computeIfAbsent(
            owner.getPublicKey(),
            k -> OctopusOwnership.forUser(owner.getPublicKey(), owner.getUserName()))
        .getOctopuses()
        .add(octo);
    return octo;
  }

  public Octopus mate(Octopus mater, Octopus partner, UserAccountData offSpringOwner) {
    val octo = octopusFactory.mate(mater, partner);
    this.octopusOwnerships
        .computeIfAbsent(
            offSpringOwner.getPublicKey(),
            k -> OctopusOwnership.forUser(offSpringOwner.getPublicKey(), offSpringOwner.getUserName()))
        .getOctopuses()
        .add(octo);
    return octo;
  }

  public boolean isOwnedBy(Octopus octopus, UserAccountData user) {
    val ownership =
        this.octopusOwnerships.getOrDefault(user.getPublicKey(), new OctopusOwnership());
    return ownership.getOctopuses().contains(octopus);
  }

  public boolean changeOwnership(Octopus octopus, UserAccountData seller, UserAccountData buyer) {
    if (!this.isOwnedBy(octopus, seller)) {
      return false;
    }

    if (!this.octopusOwnerships.get(seller.getPublicKey()).getOctopuses().remove(octopus)) {
      return false;
    }

    return this.octopusOwnerships
        .computeIfAbsent(
            buyer.getPublicKey(),
            k -> OctopusOwnership.forUser(buyer.getPublicKey(), buyer.getUserName()))
        .getOctopuses()
        .add(octopus);
  }

  public List<Octopus> getInventory(String user) {
    return this.octopusOwnerships.getOrDefault(user, new OctopusOwnership()).getOctopuses();
  }

  public List<OctopusOwnership> getOctopusOwnerships() {
    return this.octopusOwnerships.values().stream().toList();
  }

  public Optional<OctopusOwnership> getOctopusByIdResponse(String id) {
    return this.getOctopusOwnerships().stream()
        .filter(oos -> oos.getOctopuses().stream().anyMatch(octo -> octo.getIdentifier().equals(id)))
        .map(
            oos -> {
              val soos = new OctopusOwnership();
              soos.setUserName(oos.getUserName());
              soos.setPublicKey(oos.getPublicKey());
              soos.setOctopuses(
                  oos.getOctopuses().stream().filter(octo -> octo.getIdentifier().equals(id)).toList());
              return soos;
            })
        .findFirst();
  }

  public Optional<Octopus> getOctopusById(String id) {
    val optOwnership = this.getOctopusByIdResponse(id);
    if (optOwnership.isEmpty()) {
      return Optional.empty();
    }

    val octoOwnership = optOwnership.get();
    return octoOwnership.getOctopuses().stream().findFirst();
  }
}
