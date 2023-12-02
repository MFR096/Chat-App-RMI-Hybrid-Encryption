package Test;

import Cryptosystem.AsymmetricEncryption;
import Cryptosystem.KeyGenerator;
import Cryptosystem.SecureSinglePackage;
import Cryptosystem.SymmetricEncryption;
import org.junit.*;
import static org.junit.Assert.*;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Base64;

import javax.crypto.SecretKey;
public class CryptoSystemTest {

    @Test
    public void testSignature() throws SignatureException, NoSuchAlgorithmException,
            InvalidKeyException {
        String plaintext = "Hello, World!";
        SecretKey symmetricKey = KeyGenerator.generateSymmetricKey(256);
        byte[] iv = KeyGenerator.generateIV(16);
        String encryptedText = SymmetricEncryption.encryptText(plaintext, symmetricKey, iv);
        KeyPair senderKeyPair = KeyGenerator.generateAsymmetricKeyPair(2048);
        KeyPair recipientKeyPair = KeyGenerator.generateAsymmetricKeyPair(2048);
        String EncryptedSymmetricKey = AsymmetricEncryption.encryptSymmetricKey(symmetricKey,
                recipientKeyPair.getPublic());
        String encryptedIV = AsymmetricEncryption.encryptIV(iv,recipientKeyPair.getPublic());
        String securePackage = SecureSinglePackage.createSecurePackage(encryptedText, EncryptedSymmetricKey,
                encryptedIV, senderKeyPair.getPrivate());
        assertTrue(SecureSinglePackage.verifySignature(securePackage, senderKeyPair.getPublic()));
    }

    @Test
    public void testChecksumCalculation() throws SignatureException, NoSuchAlgorithmException,
            InvalidKeyException {
        String plaintext = "Hello, World!";
        SecretKey symmetricKey = KeyGenerator.generateSymmetricKey(256);
        byte[] iv = KeyGenerator.generateIV(16);
        String encryptedText = SymmetricEncryption.encryptText(plaintext, symmetricKey, iv);
        KeyPair senderKeyPair = KeyGenerator.generateAsymmetricKeyPair(2048);
        KeyPair recipientKeyPair = KeyGenerator.generateAsymmetricKeyPair(2048);
        String EncryptedSymmetricKey = AsymmetricEncryption.encryptSymmetricKey(symmetricKey,
                recipientKeyPair.getPublic());
        String encryptedIV = AsymmetricEncryption.encryptIV(iv,recipientKeyPair.getPublic());
        String securePackage = SecureSinglePackage.createSecurePackage(encryptedText, EncryptedSymmetricKey,
                encryptedIV, senderKeyPair.getPrivate());
        assertTrue(SecureSinglePackage.verifyIntegrity(securePackage));
    }

    @Test
    public void testIVManagement() {
        String plaintext = "Hello, World!";
        SecretKey symmetricKey = KeyGenerator.generateSymmetricKey(256);
        byte[] iv =KeyGenerator.generateIV(16);
        String encryptedText1 = SymmetricEncryption.encryptText(plaintext, symmetricKey, iv);
        iv =KeyGenerator.generateIV(16);
        String encryptedText2 = SymmetricEncryption.encryptText(plaintext, symmetricKey, iv);
        assertNotEquals(encryptedText1,encryptedText2);
    }

}