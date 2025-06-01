package org.micromall.userapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean enabled;
    private List<Role> roles;
    
    /**
     * Helper method to convert roles to string list for Keycloak
     * @return List of role names as strings
     */
    public List<String> getRoleNames() {
        if (roles == null) {
            return List.of();
        }
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }
    
    /**
     * Helper method to check if user has a specific role
     * @param role Role to check
     * @return true if user has the role, false otherwise
     */
    public boolean hasRole(Role role) {
        if (roles == null) {
            return false;
        }
        return roles.contains(role);
    }
    
    /**
     * Helper method to add a role to the user
     * @param role Role to add
     */
    public void addRole(Role role) {
        if (roles == null) {
            roles = new ArrayList<>();
        }
        if (!roles.contains(role)) {
            roles.add(role);
        }
    }
    
    /**
     * Helper method to set a single role (replaces any existing roles)
     * @param role Role to set
     */
    public void setRole(Role role) {
        roles = new ArrayList<>();
        roles.add(role);
    }
}