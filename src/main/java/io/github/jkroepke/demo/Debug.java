package io.github.jkroepke.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@RestController
public class Debug {
    @GetMapping("/debug/jvm")
    public Map<String, String> doJvmDebug(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Properties p = System.getProperties();
        Enumeration<Object> keys = p.keys();

        while (keys.hasMoreElements()) {
            String key = (String)keys.nextElement();
            String value = (String)p.get(key);
            map.put(key, value);
        }

        return new TreeMap<>(map);
    }

    @RequestMapping("/debug/http")
    public Map<String, Object> doHttpDebug(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();

        Map<String, String> headerMap = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            headerMap.put(key, value);
        }

        Map<String, String> parameterMap = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String value = request.getParameter(key);
            parameterMap.put(key, value);
        }

        Map<String, String> cookieMap = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie.getValue());
            }
        }

        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String value = request.getParameter(key);
            map.put(key, value);
        }

        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("getRemoteUser", request.getRemoteUser());
        requestMap.put("getMethod", request.getMethod());
        requestMap.put("getQueryString", request.getQueryString());
        requestMap.put("getAuthType", request.getAuthType());
        requestMap.put("getContextPath", request.getContextPath());
        requestMap.put("getPathInfo", request.getPathInfo());
        requestMap.put("getPathTranslated", request.getPathTranslated());
        requestMap.put("getRequestedSessionId", request.getRequestedSessionId());
        requestMap.put("getRequestURI", request.getRequestURI());
        requestMap.put("getRequestURL", request.getRequestURL().toString());
        requestMap.put("getServletPath", request.getServletPath());
        requestMap.put("getContentType", request.getContentType());
        requestMap.put("getLocalName", request.getLocalName());
        requestMap.put("getProtocol", request.getProtocol());
        requestMap.put("getRemoteAddr", request.getRemoteAddr());
        requestMap.put("getRemotePort", String.valueOf(request.getRemotePort()));
        requestMap.put("getRemoteHost", request.getRemoteHost());
        requestMap.put("getServerName", request.getServerName());


        map.put("headers", headerMap);
        map.put("cookies", cookieMap);
        map.put("parameters", parameterMap);
        map.put("request", requestMap);
        return map;
    }
}
