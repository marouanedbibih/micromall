package org.micromall.userapi.services;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.micromall.userapi.exceptions.ApiException;
import org.micromall.userapi.exceptions.UserNotFoundException;
import org.micromall.userapi.models.PageResponse;
import org.micromall.userapi.models.Role;
import org.micromall.userapi.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.NotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
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
     * Get paginated users from Keycloak with filtering and sorting capabilities
     * 
     * @param page Page number (0-based)
     * @param size Page size
     * @param search Optional search string for username, email, first or last name
     * @param sortBy Field to sort by (username, email, firstName, lastName)
     * @param sortOrder Sort order (asc or desc)
     * @param role Optional role to filter by
     * @param enabled Optional enabled status to filter by
     * @return Paginated response of users
     */
    public PageResponse<User> getPaginatedUsers(
            int page,
            int size,
            String search,
            String sortBy,
            String sortOrder,
            Role role,
            Boolean enabled
    ) {
        // Keycloak uses 1-based indexing for pagination
        int keycloakFirstResult = page * size;
        
        // Get users with pagination from Keycloak
        UsersResource usersResource = keycloak.realm(realm).users();
        
        // Get total count for pagination info (without pagination)
        // This call with count=true returns count only without users
        int totalElements = usersResource.count(search);
        
        // Get paginated users
        List<UserRepresentation> userRepresentations = usersResource.search(
                search
        );
        
        // Convert to our User model
        List<User> users = userRepresentations.stream()
                .map(this::convertToUser)
                .collect(Collectors.toList());
        
        // Filter by role if specified
        if (role != null) {
            users = users.stream()
                    .filter(user -> user.hasRole(role))
                    .collect(Collectors.toList());
        }
        
        // Filter by enabled status if specified
        if (enabled != null) {
            users = users.stream()
                    .filter(user -> user.getEnabled() != null && user.getEnabled().equals(enabled))
                    .collect(Collectors.toList());
        }
        
        // Create pagination response
        return PageResponse.of(users, page, size, totalElements);
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
    
    /**
     * Get user by ID
     * 
     * @param userId User ID
     * @return User
     * @throws UserNotFoundException If user not found
     */
    public User getUserById(String userId) {
        try {
            UserRepresentation userRepresentation = keycloak.realm(realm)
                    .users().get(userId)
                    .toRepresentation();
            
            return convertToUser(userRepresentation);
        } catch (NotFoundException e) {
            throw new UserNotFoundException(userId);
        }
    }

    /**
     * Get user by email
     * 
     * @param email User email
     * @return User
     * @throws UserNotFoundException If user not found
     */
    public User getUserByEmail(String email) {
        List<UserRepresentation> users = keycloak.realm(realm)
                .users().search(null, null, null, email, 0, 1);
        
        if (users.isEmpty()) {
            throw new UserNotFoundException("email", email);
        }
        
        return convertToUser(users.get(0));
    }
    
    /**
     * Search for users by username, email, first name, or last name
     * 
     * @param query Search query
     * @return List of matching users
     */
    public List<User> searchUsers(String query) {
        List<UserRepresentation> users = keycloak.realm(realm)
                .users().search(query, 0, 100);
                
        return users.stream()
                .map(this::convertToUser)
                .collect(Collectors.toList());
    }
    
    /**
     * Get users by role
     * 
     * @param role Role to filter by
     * @return List of users with the specified role
     */
    public List<User> getUsersByRole(Role role) {
        List<User> allUsers = getAllUsers();
        
        return allUsers.stream()
                .filter(user -> user.hasRole(role))
                .collect(Collectors.toList());
    }
    
    /**
     * Update user details
     * 
     * @param user User with updated details
     * @return Updated user
     * @throws UserNotFoundException If user not found
     */
    public User updateUser(User user) {
        if (user.getId() == null || user.getId().isEmpty()) {
            throw new ApiException("User ID cannot be null or empty", HttpStatus.BAD_REQUEST);
        }
        
        try {
            UserResource userResource = keycloak.realm(realm).users().get(user.getId());
            
            // Get the current representation
            UserRepresentation userRepresentation = userResource.toRepresentation();
            
            // Update fields
            userRepresentation.setUsername(user.getUsername());
            userRepresentation.setEmail(user.getEmail());
            userRepresentation.setFirstName(user.getFirstName());
            userRepresentation.setLastName(user.getLastName());
            
            if (user.getEnabled() != null) {
                userRepresentation.setEnabled(user.getEnabled());
            }
            
            // Update user
            userResource.update(userRepresentation);
            
            // Update roles if specified
            if (user.getRoles() != null && !user.getRoles().isEmpty()) {
                // Get all current roles
                List<RoleRepresentation> currentRoles = userResource.roles().realmLevel().listAll();
                
                // Remove all current roles
                if (!currentRoles.isEmpty()) {
                    userResource.roles().realmLevel().remove(currentRoles);
                }
                
                // Assign new roles
                assignRolesToUser(user.getId(), user.getRoleNames());
            }
            
            // Return updated user
            return getUserById(user.getId());
        } catch (NotFoundException e) {
            throw new UserNotFoundException(user.getId());
        }
    }
    
    /**
     * Delete user
     * 
     * @param userId ID of the user to delete
     * @throws UserNotFoundException If user not found
     */
    public void deleteUser(String userId) {
        try {
            Response response = keycloak.realm(realm).users().delete(userId);
            if (response.getStatus() != 204) {
                throw new ApiException(
                    "Failed to delete user. Status: " + response.getStatus(),
                    HttpStatus.INTERNAL_SERVER_ERROR
                );
            }
        } catch (NotFoundException e) {
            throw new UserNotFoundException(userId);
        }
    }
    
    /**
     * Update user password
     * 
     * @param userId User ID
     * @param newPassword New password
     * @throws UserNotFoundException If user not found
     */
    public void updatePassword(String userId, String newPassword) {
        try {
            UserResource userResource = keycloak.realm(realm).users().get(userId);
            
            // Create credential representation
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(newPassword);
            credential.setTemporary(false);
            
            // Reset password
            userResource.resetPassword(credential);
        } catch (NotFoundException e) {
            throw new UserNotFoundException(userId);
        }
    }
    
    /**
     * Enable user
     * 
     * @param userId User ID
     * @throws UserNotFoundException If user not found
     */
    public void enableUser(String userId) {
        try {
            UserResource userResource = keycloak.realm(realm).users().get(userId);
            UserRepresentation userRepresentation = userResource.toRepresentation();
            userRepresentation.setEnabled(true);
            userResource.update(userRepresentation);
        } catch (NotFoundException e) {
            throw new UserNotFoundException(userId);
        }
    }
    
    /**
     * Disable user
     * 
     * @param userId User ID
     * @throws UserNotFoundException If user not found
     */
    public void disableUser(String userId) {
        try {
            UserResource userResource = keycloak.realm(realm).users().get(userId);
            UserRepresentation userRepresentation = userResource.toRepresentation();
            userRepresentation.setEnabled(false);
            userResource.update(userRepresentation);
        } catch (NotFoundException e) {
            throw new UserNotFoundException(userId);
        }
    }
    
    /**
     * Remove role from user
     * 
     * @param userId User ID
     * @param role Role to remove
     * @throws UserNotFoundException If user not found
     */
    public void removeRoleFromUser(String userId, Role role) {
        try {
            RoleRepresentation roleToRemove = keycloak.realm(realm)
                    .roles().get(role.getName()).toRepresentation();
            
            keycloak.realm(realm)
                    .users().get(userId)
                    .roles().realmLevel()
                    .remove(Collections.singletonList(roleToRemove));
        } catch (NotFoundException e) {
            throw new UserNotFoundException(userId);
        }
    }
}
