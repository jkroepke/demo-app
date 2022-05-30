package io.github.jkroepke.demo;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
public class Load {
    Random rand = new SecureRandom();
    HashFunction hash = Hashing.goodFastHash(128);

    @GetMapping("/load")
    public Map<String, Object> doWork() {
        byte[] bytes = new byte[1024 * 1024 * 10];
        rand.nextBytes(bytes);

        HashMap<String, Object> result = new HashMap<>();
        result.put("hash", hash.hashBytes(bytes).toString());
        return result;
    }

}
