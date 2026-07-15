package com.decorcenter.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utilidad para el hashing simple de contraseñas (SHA-256).
 * En un entorno productivo se recomienda usar BCrypt.
 */
public class PasswordUtil {

    private PasswordUtil() {
    }

    public static String hash(String textoPlano) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(textoPlano.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            throw new RuntimeException("Error al generar el hash de la contraseña", e);
        }
    }
}
