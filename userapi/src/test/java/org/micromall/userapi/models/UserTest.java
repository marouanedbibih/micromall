package org.micromall.userapi.models;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void getRoleNames_shouldReturnEmptyList_whenRolesIsNull() {
        // Arrange
        User user = new User();
        user.setRoles(null);
        
        // Act
        List<String> roleNames = user.getRoleNames();
        
        // Assert
        assertTrue(roleNames.isEmpty());
    }
    
    @Test
    void getRoleNames_shouldReturnRoleNames_whenRolesExists() {
        // Arrange
        User user = new User();
        user.setRoles(Arrays.asList(Role.ADMIN, Role.CLIENT));
        
        // Act
        List<String> roleNames = user.getRoleNames();
        
        // Assert
        assertEquals(2, roleNames.size());
        assertTrue(roleNames.contains("ADMIN"));
        assertTrue(roleNames.contains("CLIENT"));
    }
    
    @Test
    void hasRole_shouldReturnFalse_whenRolesIsNull() {
        // Arrange
        User user = new User();
        user.setRoles(null);
        
        // Act & Assert
        assertFalse(user.hasRole(Role.ADMIN));
    }
    
    @Test
    void hasRole_shouldReturnTrue_whenUserHasRole() {
        // Arrange
        User user = new User();
        user.setRoles(Arrays.asList(Role.ADMIN));
        
        // Act & Assert
        assertTrue(user.hasRole(Role.ADMIN));
        assertFalse(user.hasRole(Role.CLIENT));
    }
    
    @Test
    void addRole_shouldCreateRolesList_whenRolesIsNull() {
        // Arrange
        User user = new User();
        user.setRoles(null);
        
        // Act
        user.addRole(Role.ADMIN);
        
        // Assert
        assertEquals(1, user.getRoles().size());
        assertTrue(user.hasRole(Role.ADMIN));
    }
    
    @Test
    void addRole_shouldNotAddDuplicate_whenRoleAlreadyExists() {
        // Arrange
        User user = new User();
        user.addRole(Role.ADMIN);
        int initialSize = user.getRoles().size();
        
        // Act
        user.addRole(Role.ADMIN);
        
        // Assert
        assertEquals(initialSize, user.getRoles().size());
    }
    
    @Test
    void setRole_shouldReplaceExistingRoles() {
        // Arrange
        User user = new User();
        user.setRoles(Arrays.asList(Role.ADMIN, Role.CLIENT));
        
        // Act
        user.setRole(Role.CLIENT);
        
        // Assert
        assertEquals(1, user.getRoles().size());
        assertTrue(user.hasRole(Role.CLIENT));
        assertFalse(user.hasRole(Role.ADMIN));
    }
}
