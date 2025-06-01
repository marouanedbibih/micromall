package org.micromall.userapi.services;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.micromall.userapi.models.Role;
import org.micromall.userapi.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.ws.rs.core.Response;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Service for managing users in Keycloak
 */
@Service
@RequiredArgsConstructor
public class KeycloakService {

    private final Keycloak keycloak;
    
    @Value("${keycloak.realm}")
    private String realm;
    
    /**
     * Get all users from Keycloak
     * 
     * @return List of users
     */
    public List<User> getAllUsers() {
        List<UserRepresentation> userRepresentations = keycloak.realm(realm)
                .users()
                .list();
        
        return userRepresentations.stream()
                .map(this::convertToUser)
                .collect(Collectors.toList());
    }
    
    /**
     * Create a new user in Keycloak
     * 
     * @param user User to create
     * @param password Initial password for the user
     * @return Created user with ID
     */
    public User createUser(User user, String password) {
        // Convert our User model to Keycloak's UserRepresentation
        UserRepresentation userRepresentation = convertToUserRepresentation(user);
        
        // Set up credentials
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        userRepresentation.setCredentials(Collections.singletonList(credential));
        
        // Create user in Keycloak
        UsersResource usersResource = keycloak.realm(realm).users();
        Response response = usersResource.create(userRepresentation);
        
        // Handle response
        if (response.getStatus() == 201) {
            // Extract ID from response
            String locationHeader = response.getHeaderString("Location");
            String userId = locationHeader.substring(locationHeader.lastIndexOf("/") + 1);
            user.setId(userId);
            
            // Assign roles if specified
            if (user.getRoles() != null && !user.getRoles().isEmpty()) {
                assignRolesToUser(userId, user.getRoleNames());
            }
            
            return user;
        } else {
            throw new RuntimeException("Failed to create user in Keycloak. Status: " + 
                    response.getStatus() + " " + response.getStatusInfo().getReasonPhrase());
        }
    }
    
    /**
     * Convert Keycloak UserRepresentation to our User model
     */
    private User convertToUser(UserRepresentation userRepresentation) {
        User user = new User();
        user.setId(userRepresentation.getId());
        user.setUsername(userRepresentation.getUsername());
        user.setEmail(userRepresentation.getEmail());
        user.setFirstName(userRepresentation.getFirstName());
        user.setLastName(userRepresentation.getLastName());
        user.setEnabled(userRepresentation.isEnabled());
        
        // Get user role names from Keycloak
        List<String> roleNames = keycloak.realm(realm)
                .users().get(userRepresentation.getId())
                .roles().realmLevel().listEffective()
                .stream()
                .map(RoleRepresentation::getName)
                .collect(Collectors.toList());
        
        // Convert string role names to Role enums
        List<Role> roles = new ArrayList<>();
        for (String roleName : roleNames) {
            Role role = Role.fromString(roleName);
            if (role != null) {
                roles.add(role);
            }
        }
        
        user.setRoles(roles);
        
        return user;
    }
    
    /**
     * Convert our User model to Keycloak UserRepresentation
     */
    private UserRepresentation convertToUserRepresentation(User user) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(user.getUsername());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEnabled(user.getEnabled() != null ? user.getEnabled() : true);
        userRepresentation.setEmailVerified(true);
        
        return userRepresentation;
    }
    
    /**
     * Checks if a user has a specific role
     * 
     * @param userId The ID of the user to check
     * @param role The role to check for
     * @return true if the user has the role, false otherwise
     */
    public boolean userHasRole(String userId, Role role) {
        List<RoleRepresentation> roles = keycloak.realm(realm)
                .users().get(userId)
                .roles().realmLevel().listEffective();
                
        return roles.stream()
                .anyMatch(r -> r.getName().equals(role.getName()));
    }
    
    /**
     * Assigns a specific role to a user
     * 
     * @param userId The ID of the user
     * @param role The role to assign
     */
    public void assignRoleToUser(String userId, Role role) {
        RoleRepresentation roleToAssign = keycloak.realm(realm)
                .roles().get(role.getName()).toRepresentation();
                
        keycloak.realm(realm)
                .users().get(userId)
                .roles().realmLevel()
                .add(Collections.singletonList(roleToAssign));
    }
    
    /**
     * Assign roles to a user
     */
    private void assignRolesToUser(String userId, List<String> roleNames) {
        // Get available roles in the realm
        List<RoleRepresentation> availableRoles = keycloak.realm(realm).roles().list();
        
        // Filter the roles that match the requested role names
        List<RoleRepresentation> rolesToAdd = availableRoles.stream()
                .filter(role -> roleNames.contains(role.getName()))
                .collect(Collectors.toList());
        
        // Assign roles to user
        if (!rolesToAdd.isEmpty()) {
            keycloak.realm(realm)
                    .users().get(userId)
                    .roles().realmLevel()
                    .add(rolesToAdd);
        }
    }
}
