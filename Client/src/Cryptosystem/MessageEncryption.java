package Cryptosystem;

import org.json.JSONObject;

import javax.crypto.SecretKey;
import java.security.*;

public class MessageEncryption {
    public static String encryptMessage(String plaintext, PublicKey pk, byte[] iv, PrivateKey sk) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        SecretKey symmetricKey = KeyGenerator.generateSymmetricKey(256);
        String encryptedText = SymmetricEncryption.encryptText(plaintext, symmetricKey, iv);
        String encryptedSymmetricKey = AsymmetricEncryption.encryptSymmetricKey(symmetricKey, pk);
        String encryptedIV = AsymmetricEncryption.encryptIV(iv, pk);
        return SecureSinglePackage.createSecurePackage(encryptedText, encryptedSymmetricKey, encryptedIV, sk);
    }

    public static String decryptMessage(String securePackage, PrivateKey sk){
        JSONObject json = new JSONObject(securePackage);
        String encryptedMessage = json.getString("encryptedMessage");
        String encryptedSymmetricKey = json.getString("encryptedSymmetricKey");
        byte[] iv = AsymmetricEncryption.decryptIV(json.getString("encryptedIV"), sk);
        SecretKey decryptedSymmetricKey = AsymmetricEncryption.decryptSymmetricKey(encryptedSymmetricKey, sk);
        return SymmetricEncryption.decryptText(encryptedMessage, decryptedSymmetricKey, iv);

    }
}
