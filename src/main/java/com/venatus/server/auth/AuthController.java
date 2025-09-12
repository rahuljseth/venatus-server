package com.venatus.server.auth;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController {

    @GetMapping("/auth/ping")
    public Map<String, String> ping() {
        return Map.of("status", "ok");
    }

    @GetMapping("/auth/whoami")
    public Map<String, Object> whoami(Authentication auth) {
        return Map.of(
                "username", auth.getName(),
                "authorities", auth.getAuthorities().toString()
        );
    }
}
