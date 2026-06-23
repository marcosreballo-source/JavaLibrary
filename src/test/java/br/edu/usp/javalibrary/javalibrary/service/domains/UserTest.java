package br.edu.usp.javalibrary.javalibrary.service.domains;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testUserCreationWithRandomId() {
        User user = new User("Marcos Reballo", "marcos@example.com", "e10adc3949ba59abbe56e057f20f883e");
        assertNotNull(user.getId(), "User ID should not be null when created with name, email, password.");
        assertEquals("Marcos Reballo", user.getName());
        assertEquals("marcos@example.com", user.getEmailAddress());
    }

    @Test
    public void testUserCreationWithSpecificId() {
        UUID expectedId = UUID.randomUUID();
        User user = new User(expectedId, "Arthur Azorli", "arthur@example.com", "hashpassword");
        assertEquals(expectedId, user.getId());
        assertEquals("Arthur Azorli", user.getName());
        assertEquals("arthur@example.com", user.getEmailAddress());
    }

    @Test
    public void testIsPasswordCorrect() {
        User user = new User("Pedro Kemp", "pedro@example.com", "e10adc3949ba59abbe56e057f20f883e");
        assertTrue(user.isPasswordCorrect("e10adc3949ba59abbe56e057f20f883e"), "Password should be correct when checked against correct hash.");
        assertTrue(user.isPasswordCorrect("E10ADC3949BA59ABBE56E057F20F883E"), "Password check should be case-insensitive.");
        assertFalse(user.isPasswordCorrect("wrong_hash"), "Password should be incorrect when checked against wrong hash.");
    }

    @Test
    public void testUserEquality() {
        UUID id = UUID.randomUUID();
        User user1 = new User(id, "Arthur", "arthur@example.com", "pwd");
        User user2 = new User(id, "Arthur", "arthur@example.com", "pwd");
        User user3 = new User(UUID.randomUUID(), "Arthur", "arthur@example.com", "pwd");

        assertEquals(user1, user2, "Users with same id, name, email, password should be equal.");
        assertNotEquals(user1, user3, "Users with different ids should not be equal.");
    }
}
