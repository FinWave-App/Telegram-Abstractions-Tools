package su.knst.tat.utils;

import java.security.SecureRandom;

public class CodeGenerator {
    protected static SecureRandom random = new SecureRandom();
    public static String generateRandomCode(int size) {
        return bytesToHex(random.generateSeed(size / 2));
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);

        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }
}
