package net.kal.cute.octopus.utils;

public class UUIDHelper {

  private static final String UUID_BASE = "0000-1000-8000-00805F9B34FB";
  private static final int MASK_UUID16 = 0x0000FFFF;

  public static String uuidStringFromUuid16(int uuid16) {
    StringBuilder b = new StringBuilder();
    String hex = Integer.toHexString(uuid16 & MASK_UUID16);
    b.append("00000000".substring(hex.length()));
    b.append(hex);
    b.append('-');
    b.append(UUID_BASE);

    return b.toString();
  }
}
