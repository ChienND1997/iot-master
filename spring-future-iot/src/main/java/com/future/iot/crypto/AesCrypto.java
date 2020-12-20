package com.future.iot.crypto;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;


public class AesCrypto {

    private final static Logger LOG = Logger.getLogger(AesCrypto.class);

    private SecretKeySpec skeySpec;
    private Cipher cipher;
    private IvParameterSpec iv;

    public AesCrypto(String secretKey, String initVector) {
        try {
            this.iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            skeySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
            cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        } catch (Exception e) {
            LOG.error("UnKnow padding or algorithm !");
        }
    }
    public AesCrypto(String secretKey) {
        try {
            skeySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
            cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        } catch (Exception e) {
            LOG.error("UnKnow padding or algorithm !");
        }
    }

    public String getCipherText(String plainText) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] byteEncrypted = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(byteEncrypted);
        } catch (Exception e) {
            LOG.error(e);
        }
        return "";
    }

    public String getPlainText(String cipherText)  {
        try {
            byte[] byteCipherText = Base64.getDecoder().decode(cipherText);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] byteDecrypted = cipher.doFinal(byteCipherText);
            return new String(byteDecrypted);
        }catch (Exception e){
            LOG.error(e);
        }
        return "";
    }

    public static void main(String[] args) {
        AesCrypto aesCryptoCBC = new AesCrypto("chdSXsWOLryoQCUp", "67HWgqAL9uga13tY");
        String cipher = aesCryptoCBC.getCipherText("35.3-45");
        System.out.println(cipher);
        String plain = aesCryptoCBC.getPlainText(cipher);
        System.out.println(plain);
    }
}
