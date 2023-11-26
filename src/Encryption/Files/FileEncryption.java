package Encryption.Files;


import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class FileEncryption {

    public static void main(String[] args) {
        try {
            String originalText = "This is a secret message!";
            Path filePath = Paths.get("secret.txt");

            // Generate a random key
            SecretKey secretKey = generateKey();

            // Encrypt the text and write to a file
            encryptTextToFile(originalText, filePath, secretKey);

            // Read encrypted text from the file and decrypt it
            String decryptedText = decryptTextFromFile(filePath, secretKey);
            System.out.println("Decrypted Text: " + decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Function to generate a random secret key
    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256); // Choose the key size here, e.g., 128, 192, or 256
        return keyGenerator.generateKey();
    }
    // Function to encrypt text and write to a file
    public static void encryptTextToFile(String text, Path filePath, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] iv = cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
        byte[] encryptedText = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));

        String encodedText = Base64.getEncoder().encodeToString(encryptedText);
        Files.write(filePath, encodedText.getBytes(StandardCharsets.UTF_8));

        System.out.println("Text encrypted and written to file: " + filePath);
    }

    // Function to decrypt text from a file
    public static String decryptTextFromFile(Path filePath, SecretKey secretKey) throws Exception {
        byte[] encryptedText = Files.readAllBytes(filePath);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(new byte[16]));

        byte[] decodedEncryptedText = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedText = cipher.doFinal(decodedEncryptedText);

        return new String(decryptedText, StandardCharsets.UTF_8);
    }
}
