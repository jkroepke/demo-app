package io.github.jkroepke.demo.controller;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Enumeration;
import java.util.Objects;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static java.util.stream.Collectors.joining;

@RestController
public class Connect {
    @RequestMapping("/connect/http")
    public Mono<ResponseEntity<String>> http(@RequestParam("url") URI uri,
                                             @RequestBody(required = false) String body,
                                             HttpMethod method, HttpServletRequest request, HttpServletResponse response) {

        WebClient client = WebClient.create(uri.getHost());
        WebClient.RequestHeadersSpec<?> headersSpec = client.method(method).uri(uri).bodyValue(body == null ? "" : body);

        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (headerName.equalsIgnoreCase("host")) continue;
            headersSpec.header(headerName, request.getHeader(headerName));
        }

        return headersSpec.exchangeToMono(webClientResponse -> webClientResponse.toEntity(String.class));
    }

    @RequestMapping(value = "/connect/head", produces = "text/plain;charset=UTF-8")
    public String head(@RequestParam("url") URI uri,
                       @RequestBody(required = false) String body,
                       HttpMethod method, HttpServletRequest request, HttpServletResponse response) {

        WebClient client = WebClient.create(uri.getHost());
        WebClient.RequestHeadersSpec<?> headersSpec = client.method(HttpMethod.HEAD).uri(uri).bodyValue(body == null ? "" : body);

        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (headerName.equalsIgnoreCase("host")) continue;
            headersSpec.header(headerName, request.getHeader(headerName));
        }

        return Objects.requireNonNull(headersSpec.exchangeToMono(webClientResponse -> webClientResponse.toEntity(String.class)).block())
            .getHeaders().entrySet().stream()
            .map(Object::toString).collect(joining("\n"));
    }
}
