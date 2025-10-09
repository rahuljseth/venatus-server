package com.venatus.server.capabilities;

import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CapabilitiesLoader {

    private final Map<String, Set<String>> roleToCaps;

    public CapabilitiesLoader() {
        this.roleToCaps = Collections.unmodifiableMap(loadFromYaml());
    }

    @SuppressWarnings("unchecked")
    private Map<String, Set<String>> loadFromYaml() {
        YamlMapFactoryBean yaml = new YamlMapFactoryBean();
        yaml.setResources(new ClassPathResource("capabilities.yml"));

        Map<String, Object> root = yaml.getObject();
        if (root == null) return Map.of();

        // Prefer "roles:" section if present; otherwise treat the root as the map (defensive)
        Object rolesObj = root.containsKey("roles") ? root.get("roles") : root;
        if (!(rolesObj instanceof Map)) return Map.of();

        Map<String, Object> roles = (Map<String, Object>) rolesObj;
        Map<String, Set<String>> out = new LinkedHashMap<>();

        for (Map.Entry<String, Object> e : roles.entrySet()) {
            String roleKey = e.getKey().toLowerCase(Locale.ROOT).trim();
            Set<String> caps = new LinkedHashSet<>();
            Object val = e.getValue();
            if (val instanceof Collection<?> coll) {
                for (Object item : coll) {
                    if (item != null) caps.add(String.valueOf(item).trim());
                }
            }
            out.put(roleKey, Collections.unmodifiableSet(caps));
        }
        return out;
    }

    public Set<String> getCapsForRole(String role) {
        if (role == null) return Set.of();
        return roleToCaps.getOrDefault(role.toLowerCase(Locale.ROOT).trim(), Set.of());
    }

    public Set<String> getCapsForRoles(Collection<String> roles) {
        if (roles == null || roles.isEmpty()) return Set.of();
        Set<String> union = new LinkedHashSet<>();
        for (String r : roles) union.addAll(getCapsForRole(r));
        return union;
    }

    public Map<String, Set<String>> getPolicy() {
        return roleToCaps;
    }
}
