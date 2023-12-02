package Cryptosystem;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class KeyGenerator {

    public static KeyPair generateAsymmetricKeyPair(int size) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(size);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] generateIV(int size) {
        SecureRandom random = new SecureRandom();
        byte[] iv = new byte[size];
        random.nextBytes(iv);
        return iv;
    }

    public static SecretKey generateSymmetricKey(int size) {
        try {
            javax.crypto.KeyGenerator keyGen = javax.crypto.KeyGenerator.getInstance("AES");
            keyGen.init(size);
            return keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}