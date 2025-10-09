package com.venatus.server.auth;

import java.util.List;

public record WhoAmIResponse(
        String username,
        List<String> roles,
        List<String> capabilities
) {}
