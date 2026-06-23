package br.edu.usp.javalibrary.javalibrary.service.utils;

import org.junit.jupiter.api.Test;
import java.security.NoSuchAlgorithmException;
import static org.junit.jupiter.api.Assertions.*;

public class CriptoTest {

    @Test
    public void testGetMD5Success() throws NoSuchAlgorithmException {
        String input = "123456";
        String expectedHash = "e10adc3949ba59abbe56e057f20f883e"; // MD5 hash for "123456"
        String actualHash = Cripto.getMD5(input);
        assertEquals(expectedHash, actualHash, "The MD5 hash of '123456' is incorrect.");
    }

    @Test
    public void testGetMD5EmptyString() throws NoSuchAlgorithmException {
        String input = "";
        String expectedHash = "d41d8cd98f00b204e9800998ecf8427e"; // MD5 hash for empty string
        String actualHash = Cripto.getMD5(input);
        assertEquals(expectedHash, actualHash, "The MD5 hash of empty string is incorrect.");
    }

    @Test
    public void testGetMD5DifferentInputs() throws NoSuchAlgorithmException {
        String input1 = "hello";
        String input2 = "world";
        assertNotEquals(Cripto.getMD5(input1), Cripto.getMD5(input2), "Different inputs should produce different MD5 hashes.");
    }
}
