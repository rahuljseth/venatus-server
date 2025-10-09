package com.venatus.server.auth;

import com.venatus.server.capabilities.ICapabilitiesService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final ICapabilitiesService capabilitiesService;

    public AuthController(ICapabilitiesService capabilitiesService) {
        this.capabilitiesService = capabilitiesService;
    }

    @GetMapping("/ping")
    public Map<String, String> ping() {
        return Map.of("status", "ok");
    }

    @GetMapping("/whoami")
    public WhoAmIResponse whoAmI(Authentication auth) {
        String username = auth.getName();

        // e.g. ROLE_SUPERADMIN → "superadmin"
        List<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(this::normalizeRole)
                .filter(s -> !s.isBlank())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        // union of capabilities for all roles (order preserved from YAML)
        List<String> capabilities = capabilitiesService.unionList(roles);

        return new WhoAmIResponse(username, roles, capabilities);
    }

    private String normalizeRole(String authority) {
        if (authority == null) return "";
        String r = authority;
        if (r.startsWith("ROLE_")) r = r.substring(5);
        return r.toLowerCase(Locale.ROOT).trim();
    }
}
