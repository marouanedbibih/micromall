package org.micromall.userapi.controllers;

import lombok.RequiredArgsConstructor;
import org.micromall.userapi.models.PageResponse;
import org.micromall.userapi.models.Role;
import org.micromall.userapi.models.User;
import org.micromall.userapi.services.KeycloakService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST controller for user management operations
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final KeycloakService keycloakService;

    /**
     * Get paginated users with filtering and sorting
     * 
     * @param page Page number (1-based), default 1
     * @param size Page size, default 10
     * @param search Search query for username, email, first or last name
     * @param sortBy Field to sort by (username, email, firstName, lastName)
     * @param sortOrder Sort order (asc or desc)
     * @param roleStr Role filter as string
     * @param enabled Enabled status filter
     * @return Paginated list of users
     */
    @GetMapping
    public ResponseEntity<PageResponse<User>> getUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "username") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) String roleStr,
            @RequestParam(required = false) Boolean enabled
    ) {
        // Convert role string to enum if provided
        Role role = null;
        if (roleStr != null && !roleStr.isEmpty()) {
            role = Role.fromString(roleStr);
        }
        
        // Convert 1-based page to 0-based for service layer
        int zeroBasedPage = Math.max(0, page - 1);
        
        // Get paginated users
        PageResponse<User> paginatedUsers = keycloakService.getPaginatedUsers(
                zeroBasedPage, size, search, sortBy, sortOrder, role, enabled
        );
        
        // Convert response to use 1-based pagination for API consumers
        adjustPageResponseToOneBased(paginatedUsers);
        
        return ResponseEntity.ok(paginatedUsers);
    }
    
    /**
     * Get all users (non-paginated)
     * @return List of users
     */
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = keycloakService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Create a new user
     * @param userRequest Map containing user details and role
     * @return Created user
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody Map<String, Object> userRequest) {
        // Extract user data from request
        User user = new User();
        user.setUsername((String) userRequest.get("username"));
        user.setEmail((String) userRequest.get("email"));
        user.setFirstName((String) userRequest.get("firstName"));
        user.setLastName((String) userRequest.get("lastName"));
        user.setEnabled((Boolean) userRequest.getOrDefault("enabled", true));
        
        // Get role from request or default to CLIENT
        String roleStr = (String) userRequest.getOrDefault("role", "CLIENT");
        Role role = Role.fromString(roleStr);
        
        // Validate role
        if (role == null) {
            return ResponseEntity.badRequest().build();
        }
        
        // Set role
        user.setRole(role);
        
        // Extract password
        String password = (String) userRequest.get("password");
        if (password == null || password.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        // Create user
        User createdUser = keycloakService.createUser(user, password);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    
    /**
     * Get all admins
     * 
     * @return List of users with ADMIN role
     */
    @GetMapping("/admins")
    public ResponseEntity<List<User>> getAdmins() {
        List<User> users = keycloakService.getAllUsers();
        
        List<User> admins = users.stream()
                .filter(user -> user.hasRole(Role.ADMIN))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(admins);
    }
    
    /**
     * Get all clients
     * 
     * @return List of users with CLIENT role
     */
    @GetMapping("/clients")
    public ResponseEntity<List<User>> getClients() {
        List<User> users = keycloakService.getAllUsers();
        
        List<User> clients = users.stream()
                .filter(user -> user.hasRole(Role.CLIENT))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(clients);
    }
    
    /**
     * Get user by ID
     * 
     * @param id User ID
     * @return User
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        User user = keycloakService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    
    /**
     * Search for users
     * 
     * @param query Search query
     * @return List of matching users
     */
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String query) {
        List<User> users = keycloakService.searchUsers(query);
        return ResponseEntity.ok(users);
    }
    
    /**
     * Get user by email
     * 
     * @param email User email
     * @return User
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = keycloakService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }
    
    /**
     * Update user
     * 
     * @param id User ID
     * @param userRequest Map containing user update details
     * @return Updated user
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable String id,
            @RequestBody Map<String, Object> userRequest
    ) {
        // First get the existing user
        User existingUser = keycloakService.getUserById(id);
        
        // Update fields if provided in the request
        if (userRequest.containsKey("username")) {
            existingUser.setUsername((String) userRequest.get("username"));
        }
        if (userRequest.containsKey("email")) {
            existingUser.setEmail((String) userRequest.get("email"));
        }
        if (userRequest.containsKey("firstName")) {
            existingUser.setFirstName((String) userRequest.get("firstName"));
        }
        if (userRequest.containsKey("lastName")) {
            existingUser.setLastName((String) userRequest.get("lastName"));
        }
        if (userRequest.containsKey("enabled")) {
            existingUser.setEnabled((Boolean) userRequest.get("enabled"));
        }
        
        // Update role if provided
        if (userRequest.containsKey("role")) {
            String roleStr = (String) userRequest.get("role");
            Role role = Role.fromString(roleStr);
            if (role != null) {
                existingUser.setRole(role);
            }
        }
        
        // Update user
        User updatedUser = keycloakService.updateUser(existingUser);
        return ResponseEntity.ok(updatedUser);
    }
    
    /**
     * Delete user
     * 
     * @param id User ID
     * @return Empty response with 204 status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        keycloakService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Update user password
     * 
     * @param id User ID
     * @param passwordUpdate Map containing new password
     * @return Empty response with 204 status
     */
    @PutMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(
            @PathVariable String id,
            @RequestBody Map<String, String> passwordUpdate
    ) {
        String newPassword = passwordUpdate.get("password");
        if (newPassword == null || newPassword.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        keycloakService.updatePassword(id, newPassword);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Enable user
     * 
     * @param id User ID
     * @return User with updated enabled status
     */
    @PutMapping("/{id}/enable")
    public ResponseEntity<User> enableUser(@PathVariable String id) {
        keycloakService.enableUser(id);
        return ResponseEntity.ok(keycloakService.getUserById(id));
    }
    
    /**
     * Disable user
     * 
     * @param id User ID
     * @return User with updated enabled status
     */
    @PutMapping("/{id}/disable")
    public ResponseEntity<User> disableUser(@PathVariable String id) {
        keycloakService.disableUser(id);
        return ResponseEntity.ok(keycloakService.getUserById(id));
    }
    
    /**
     * Assign role to user
     * 
     * @param id User ID
     * @param roleRequest Map containing role to assign
     * @return User with updated roles
     */
    @PostMapping("/{id}/roles")
    public ResponseEntity<User> assignRoleToUser(
            @PathVariable String id,
            @RequestBody Map<String, String> roleRequest
    ) {
        String roleStr = roleRequest.get("role");
        if (roleStr == null || roleStr.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        Role role = Role.fromString(roleStr);
        if (role == null) {
            return ResponseEntity.badRequest().build();
        }
        
        keycloakService.assignRoleToUser(id, role);
        return ResponseEntity.ok(keycloakService.getUserById(id));
    }
    
    /**
     * Remove role from user
     * 
     * @param id User ID
     * @param roleStr Role to remove
     * @return User with updated roles
     */
    @DeleteMapping("/{id}/roles/{role}")
    public ResponseEntity<User> removeRoleFromUser(
            @PathVariable String id,
            @PathVariable String roleStr
    ) {
        Role role = Role.fromString(roleStr);
        if (role == null) {
            return ResponseEntity.badRequest().build();
        }
        
        keycloakService.removeRoleFromUser(id, role);
        return ResponseEntity.ok(keycloakService.getUserById(id));
    }
    
    /**
     * Adjust a PageResponse from 0-based pagination (internal) to 1-based pagination (API)
     * 
     * @param pageResponse The PageResponse to adjust
     * @param <T> The type of elements in the page
     */
    private <T> void adjustPageResponseToOneBased(PageResponse<T> pageResponse) {
        if (pageResponse == null) {
            return;
        }
        
        // Convert page number from 0-based to 1-based
        pageResponse.setPageNumber(pageResponse.getPageNumber() + 1);
        
        // Adjust first/last flags if needed
        pageResponse.setFirst(pageResponse.getPageNumber() == 1);
        pageResponse.setLast(pageResponse.getPageNumber() >= pageResponse.getTotalPages());
    }
}
