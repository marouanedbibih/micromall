package org.micromall.userapi.controllers;

import lombok.RequiredArgsConstructor;
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
     * Get all users
     * @param roleStr Optional role to filter by
     * @return List of users
     */
    @GetMapping
    public ResponseEntity<List<User>> getUsers(@RequestParam(required = false) String roleStr) {
        List<User> users = keycloakService.getAllUsers();
        
        // Filter by role if specified
        if (roleStr != null && !roleStr.isEmpty()) {
            Role role = Role.fromString(roleStr);
            if (role != null) {
                users = users.stream()
                        .filter(user -> user.hasRole(role))
                        .collect(Collectors.toList());
            }
        }
        
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
}
