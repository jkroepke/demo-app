package io.github.jkroepke.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
public class Load {
    Random rand = new SecureRandom();
    static MessageDigest messageDigest;

    static {
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/load")
    public Map<String, Object> doWork() {
        byte[] bytes = new byte[1024 * 1024 * 10];
        rand.nextBytes(bytes);

        HashMap<String, Object> result = new HashMap<>();
        result.put("hash", new String(messageDigest.digest(bytes)));
        return result;
    }

}
