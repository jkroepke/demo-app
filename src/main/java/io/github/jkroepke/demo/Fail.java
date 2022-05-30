package io.github.jkroepke.demo;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import java.security.SecureRandom;
import java.util.Objects;

@RestController
public class Fail {
    private final Counter counterFail;
    private final Counter counterSuccess;

    private final SecureRandom secureRandom = new SecureRandom();

    @Autowired
    public Fail(MeterRegistry registry) {
        this.counterFail = registry.counter("devops_api_service_fail", "fail", "1");
        this.counterSuccess = registry.counter("devops_api_service_fail", "fail", "0");
    }

    @GetMapping("/fail")
    public String getError() {
        if(Objects.equals(secureRandom.nextInt(4), 0)) {
            counterFail.increment();
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        counterSuccess.increment();
        return "OK";
    }
}
