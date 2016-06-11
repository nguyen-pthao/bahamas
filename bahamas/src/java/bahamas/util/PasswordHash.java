/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;
/**
 *
 * @author HUXLEY
 */
public class PasswordHash {

    // The number of hash iterations
    private static final int iterations = 1000;

    // Algorithm is either MD5, SHA-1, SHA-256 (MD5 is discouraged)
    private static final String algorithm = "SHA-256";

    private static String base = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static Random random = new Random();

    public static String[] getHashAndSalt(String password) {
        // Create a random salt, returning 64-bit (8 bytes) of binary data
        SecureRandom sr = new SecureRandom();
        byte[] bSalt = new byte[8];
        sr.nextBytes(bSalt);

        String[] result = new String[2];

        try {
            // Select the digest for the hash computation
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.reset();
            // Pass the salt to the digest for the computation
            digest.update(bSalt);
            byte[] bDigest = digest.digest(password.getBytes("UTF-8"));

            // Iterate (makes brute-force computation more time consuming)
            for (int i = 0; i < iterations; i++) {
                digest.reset();
                bDigest = digest.digest(bDigest);
            }

            result[0] = Base64.getEncoder().encodeToString(bDigest);
            result[1] = Base64.getEncoder().encodeToString(bSalt);

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            System.out.println("No such Algorithm or Encoding.");
        }

        return result;
    }

    public static boolean verify(String enteredPassword, String hash, String salt) {
        byte[] bSalt = Base64.getDecoder().decode(salt);

        try {
            // Select the digest for the hash computation
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.reset();
            // Pass the salt to the digest for the computation
            digest.update(bSalt);
            byte[] bDigest = digest.digest(enteredPassword.getBytes("UTF-8"));

            // Iterate (makes brute-force computation more time consuming)
            for (int i = 0; i < iterations; i++) {
                digest.reset();
                bDigest = digest.digest(bDigest);
            }

            String enteredHash = Base64.getEncoder().encodeToString(bDigest);

            return (enteredHash.equals(hash));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            System.out.println("No such Algorithm or Encoding.");
        }

        return false;
    }

    public static String generateRandomString(int length) {
        StringBuilder b = new StringBuilder();

        for (int i = 0; i < length; i++) {
            b.append(base.charAt(random.nextInt(base.length())));
        }

        return b.toString();
    }
}
