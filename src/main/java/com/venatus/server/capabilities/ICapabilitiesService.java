package com.venatus.server.capabilities;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ICapabilitiesService {
    Set<String> unionForRoles(Collection<String> roles);
    List<String> unionList(Collection<String> roles);
    Map<String, Set<String>> policy(); // optional: for diagnostics
}
