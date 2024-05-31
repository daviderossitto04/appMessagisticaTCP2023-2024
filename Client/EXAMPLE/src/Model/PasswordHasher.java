package Model;

import java.security.MessageDigest;

public class PasswordHasher {

    // Metodo per hashare una password con SHA-256
    public static String hashPasswordWithSHA256(String password) {
        return hashPassword(password, "SHA-256");
    }

    // Metodo per hashare una password con MD5
    public static String hashPasswordWithMD5(String password) {
        return hashPassword(password, "MD5");
    }

    // Metodo generico per hashare una password con l'algoritmo specificato
    private static String hashPassword(String password, String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] encodedHash = digest.digest(password.getBytes("UTF-8"));
            return bytesToHex(encodedHash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Metodo per convertire byte array in stringa esadecimale
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static void main(String[] args) {
        // Esempio di utilizzo della classe
        String password = "cavolo";

        // Hash della password con SHA-256
        String sha256HashedPassword = hashPasswordWithSHA256(password);
        System.out.println("Password originale: " + password);
        System.out.println("Password hashata (SHA-256): " + sha256HashedPassword);

        if(sha256HashedPassword.equals(hashPasswordWithSHA256(password))) {
        	System.out.println("ciao");
        }
        // Hash della password con MD5
        String md5HashedPassword = hashPasswordWithMD5(password);
        System.out.println("Password hashata (MD5): " + md5HashedPassword);
    }
}
