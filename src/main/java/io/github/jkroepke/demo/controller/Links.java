package io.github.jkroepke.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Links {    private static final String HTML = """
    <!DOCTYPE html>
    <html>
        <head>
            <title>Links</title>
        </head>
        <body>
            <ul>
                <li><a href="/files">File Upload</a></li>
                <li><a href="/connect/head?url=https://example.com">HTTP Connect (HEAD)</a></li>
                <li><a href="/connect/http?url=https://example.com">HTTP Connect</a></li>
                <li><a href="/debug/http">Debug HTTP Header</a></li>
                <li><a href="/debug/cgroup">Debug cgroup</a></li>
                <li><a href="/debug/jvm">Debug JVM</a></li>
                <li><a href="/fail">Random Fail</a></li>
                <li><a href="/load">Generate CPU Load</a></li>
                <li><a href="/actuator/prometheus">Prometheus endpoint</a></li>
            </ul>
        </body>
    </html>""";

    @GetMapping(value = "/links", produces = "text/html;charset=UTF-8")
    public String index() {
        return HTML;
    }
}
