package com.datastax.training.killrvideo.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Created on 19/10/2015.
 */
public class AuthenticationUtils {

    private static Random RANDOM = new SecureRandom();

    public static byte[] hash(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        byte[] hash = digest.digest(password.getBytes("UTF-8"));
        return hash;
    }

    public static byte[] hash(String password, byte[] salt)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        digest.update(salt);
        byte[] hash = digest.digest(password.getBytes("UTF-8"));
        return hash;
    }

    public static byte[] getRandomSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return salt;
    }

}
