package br.edu.usp.javalibrary.javalibrary.service.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Cripto {

    public static String getMD5(String input) throws NoSuchAlgorithmException {
        final MessageDigest converter = MessageDigest.getInstance("MD5");
        byte[] digest = converter.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder builder = new StringBuilder();
        for (byte it : digest) {
            builder.append(String.format("%02x", it));
        }
        return builder.toString();
    }
}
