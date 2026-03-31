package com.example.tripplanner.security;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Deriva y verifica contraseñas con PBKDF2-HMAC-SHA256. El valor guardado en Room
 * no es la contraseña en claro.
 * <p>
 * Formato almacenado: {@code pbkdf2_sha256$<iteraciones>$<salt_b64>$<hash_b64>}
 */
public final class PasswordHasher {

    private static final String SCHEME = "pbkdf2_sha256";
    private static final int ITERATIONS = 100_000;
    private static final int SALT_BYTES = 16;
    private static final int KEY_BITS = 256;

    private PasswordHasher() {
    }

    /**
     * Genera un string para guardar en {@code password_hash} (no reversible).
     */
    public static String hash(String plainPassword) {
        if (plainPassword == null) {
            throw new IllegalArgumentException("password");
        }
        byte[] salt = new byte[SALT_BYTES];
        new SecureRandom().nextBytes(salt);
        try {
            byte[] hash = pbkdf2(plainPassword, salt, ITERATIONS);
            return SCHEME + "$" + ITERATIONS + "$"
                    + Base64.encodeToString(salt, Base64.NO_WRAP) + "$"
                    + Base64.encodeToString(hash, Base64.NO_WRAP);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("PBKDF2 no disponible", e);
        }
    }

    /**
     * Comprueba la contraseña contra el valor almacenado (hash o legado en texto plano).
     */
    public static boolean verify(String plainPassword, String stored) {
        if (plainPassword == null || stored == null) {
            return false;
        }
        if (isPbkdf2Stored(stored)) {
            return verifyPbkdf2(plainPassword, stored);
        }
        // Migración: datos antiguos guardaban la contraseña sin hash
        return plainPassword.equals(stored);
    }

    /**
     * {@code true} si hay que sustituir el valor por un hash (texto plano u otro formato).
     */
    public static boolean needsMigration(String stored) {
        return stored == null || !isPbkdf2Stored(stored);
    }

    private static boolean isPbkdf2Stored(String stored) {
        return stored.startsWith(SCHEME + "$");
    }

    private static boolean verifyPbkdf2(String plainPassword, String stored) {
        try {
            String[] parts = stored.split("\\$", 4);
            if (parts.length != 4 || !SCHEME.equals(parts[0])) {
                return false;
            }
            int iterations = Integer.parseInt(parts[1]);
            byte[] salt = Base64.decode(parts[2], Base64.NO_WRAP);
            byte[] expected = Base64.decode(parts[3], Base64.NO_WRAP);
            byte[] actual = pbkdf2(plainPassword, salt, iterations);
            if (expected.length != actual.length) {
                return false;
            }
            return slowEquals(expected, actual);
        } catch (IllegalArgumentException | GeneralSecurityException e) {
            return false;
        }
    }

    private static byte[] pbkdf2(String password, byte[] salt, int iterations)
            throws GeneralSecurityException {
        KeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                salt,
                iterations,
                KEY_BITS
        );
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        return skf.generateSecret(spec).getEncoded();
    }

    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        int len = Math.min(a.length, b.length);
        for (int i = 0; i < len; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }
}
