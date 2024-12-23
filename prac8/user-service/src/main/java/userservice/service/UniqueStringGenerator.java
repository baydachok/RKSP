package userservice.service;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Component
public class UniqueStringGenerator {

    public String generateUniqueString(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hashString = new StringBuilder();

            for (byte b : hashBytes) {
                hashString.append(String.format("%02x", b));
            }
            long timestamp = System.currentTimeMillis();

            return hashString + "_" + timestamp;

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating unique string", e);
        }
    }
}