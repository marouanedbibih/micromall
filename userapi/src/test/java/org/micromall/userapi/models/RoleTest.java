package org.micromall.userapi.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @Test
    void getName_shouldReturnCorrectName() {
        // Act & Assert
        assertEquals("ADMIN", Role.ADMIN.getName());
        assertEquals("CLIENT", Role.CLIENT.getName());
    }

    @Test
    void fromString_shouldReturnCorrectEnum_whenValidNameProvided() {
        // Act & Assert
        assertEquals(Role.ADMIN, Role.fromString("ADMIN"));
        assertEquals(Role.CLIENT, Role.fromString("CLIENT"));
    }

    @Test
    void fromString_shouldBeCaseInsensitive() {
        // Act & Assert
        assertEquals(Role.ADMIN, Role.fromString("admin"));
        assertEquals(Role.CLIENT, Role.fromString("client"));
    }

    @Test
    void fromString_shouldReturnNull_whenInvalidNameProvided() {
        // Act & Assert
        assertNull(Role.fromString("INVALID_ROLE"));
        assertNull(Role.fromString(""));
        assertNull(Role.fromString(null));
    }
}
