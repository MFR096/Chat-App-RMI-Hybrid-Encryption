package Cryptosystem;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class AsymmetricEncryption {

    public static String encryptSymmetricKey(SecretKey symmetricKey,PublicKey recipientPublicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, recipientPublicKey);
            byte[] encryptedSymmetricKeyForSender = cipher.doFinal(symmetricKey.getEncoded());
            return Base64.getEncoder().encodeToString(encryptedSymmetricKeyForSender);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encryptIV(byte[] iv, PublicKey recipientPublicKey) {
        try {
            Cipher rsaCipher = Cipher.getInstance("RSA");
            rsaCipher.init(Cipher.ENCRYPT_MODE, recipientPublicKey);
            byte[] encryptedIV = rsaCipher.doFinal(iv);
            return Base64.getEncoder().encodeToString(encryptedIV);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static SecretKey decryptSymmetricKey(String encryptedSymmetricKey, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedSymmetricKeyBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedSymmetricKey));
            SecretKey secretKey = new SecretKeySpec(decryptedSymmetricKeyBytes, "AES");
            return secretKey;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decryptIV(String encryptedIV, PrivateKey recipientPrivateKey) {
        try {
            Cipher rsaCipher = Cipher.getInstance("RSA");
            rsaCipher.init(Cipher.DECRYPT_MODE, recipientPrivateKey);
            byte[] iv = rsaCipher.doFinal(Base64.getDecoder().decode(encryptedIV));
            return iv;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
