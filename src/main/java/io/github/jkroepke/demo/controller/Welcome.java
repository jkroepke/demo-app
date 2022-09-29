package io.github.jkroepke.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Welcome {
    @Value("${color.background}")
    private String backgroundColor;

    @Value("${color.font}")
    private String fontColor;

    @Value("${body.text}")
    private String bodyText;

    private static final String HTML = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "    <head>\n" +
            "        <title>Spring Boot Demo</title>\n" +
            "    </head>\n" +
            "    <body style=\"color: %s ; background: %s ; width: 100%%; height: 100%%;\">\n" +
            "       <h1>%s</h1>" +
            "    </body>\n" +
            "</html>";

    @GetMapping(value = "/", produces = "text/html;charset=UTF-8")
    public String index() {
        return String.format(HTML, fontColor, backgroundColor, bodyText);
    }
}
