package com.venatus.server.capabilities;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CapabilitiesService implements ICapabilitiesService {

    private final CapabilitiesLoader loader;

    public CapabilitiesService(CapabilitiesLoader loader) {
        this.loader = loader;
    }

    @Override
    public Set<String> unionForRoles(Collection<String> roles) {
        return loader.getCapsForRoles(roles);
    }

    @Override
    public List<String> unionList(Collection<String> roles) {
        // Keep order stable for UI (LinkedHashSet in loader preserves YAML order)
        return new ArrayList<>(unionForRoles(roles));
    }

    @Override
    public Map<String, Set<String>> policy() {
        return loader.getPolicy();
    }
}
