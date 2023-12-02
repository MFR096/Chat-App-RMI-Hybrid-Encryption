package Cryptosystem;

import org.json.JSONObject;

import java.security.*;
import java.util.Base64;

public class SecureSinglePackage {

    public static String createSecurePackage(String encryptedMessage, String encryptedSymmetricKey, String encryptedIV, PrivateKey senderPrivateKey) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        String integrityCheck = calculateIntegrityCheck(encryptedMessage + encryptedSymmetricKey);
        JSONObject securePackage = new JSONObject();
        securePackage.put("encryptedMessage", encryptedMessage);
        securePackage.put("encryptedSymmetricKey", encryptedSymmetricKey);
        securePackage.put("encryptedIV", encryptedIV);
        securePackage.put("integrityCheck", integrityCheck);
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(senderPrivateKey);
        signature.update((encryptedMessage + encryptedSymmetricKey + encryptedIV).getBytes());
        byte[] signedData = signature.sign();
        securePackage.put("signature", Base64.getEncoder().encodeToString(signedData));
        return securePackage.toString();
    }

    public static boolean verifySignature(String securePackage, PublicKey senderPublicKey) {
        try {
            JSONObject jsonObject = new JSONObject(securePackage);
            String encryptedMessage = jsonObject.getString("encryptedMessage");
            String encryptedSymmetricKey = jsonObject.getString("encryptedSymmetricKey");
            String ivString = jsonObject.getString("encryptedIV");
            String signatureString = jsonObject.getString("signature");
            byte[] iv = Base64.getDecoder().decode(ivString);
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(senderPublicKey);
            signature.update((encryptedMessage + encryptedSymmetricKey + ivString).getBytes());
            byte[] signatureBytes = Base64.getDecoder().decode(signatureString);
            return signature.verify(signatureBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean verifyIntegrity(String receivedPackage) {
        try {
            JSONObject json = new JSONObject(receivedPackage);
            String encryptedMessage = json.getString("encryptedMessage");
            String encryptedSymmetricKey = json.getString("encryptedSymmetricKey");
            String receivedIntegrityCheck = json.getString("integrityCheck");
            String calculatedIntegrityCheck = calculateIntegrityCheck(encryptedMessage + encryptedSymmetricKey);
            return calculatedIntegrityCheck.equals(receivedIntegrityCheck);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String calculateIntegrityCheck(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((data).getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
