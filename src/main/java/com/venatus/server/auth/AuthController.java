package com.venatus.server.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class AuthController {

    @GetMapping("/auth/ping")
    public Map<String, String> ping() {
        return Map.of("status", "ok");
    }

    @GetMapping("/auth/whoami")
    public Map<String, Object> whoAmI(Authentication auth) {
        // Example authorities from Spring: ROLE_SUPERADMIN, ROLE_RECRUITER, etc.
        List<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)       // "ROLE_SUPERADMIN"
                .map(s -> s.replaceFirst("^ROLE_", ""))    // "SUPERADMIN"
                .map(String::toLowerCase)                  // "superadmin"
                .toList();

        return Map.of(
                "username", auth.getName(),
                "roles", roles
        );
    }
}
