package org.micromall.userapi.models;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum representing the available roles in the system
 */
public enum Role {
    ADMIN("ADMIN"),
    CLIENT("CLIENT");
    
    private final String name;
    
    Role(String name) {
        this.name = name;
    }
    
    @JsonValue
    public String getName() {
        return name;
    }
    
    /**
     * Convert string role name to Role enum
     * @param roleName String representation of role
     * @return Role enum or null if not found
     */
    public static Role fromString(String roleName) {
        for (Role role : Role.values()) {
            if (role.name.equalsIgnoreCase(roleName)) {
                return role;
            }
        }
        return null;
    }
}
