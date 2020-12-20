package com.future.iot.crypto;

import com.future.iot.model.KeysManageDevice;
import com.future.iot.repo.KeysManageDeviceRepository;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;


@Component
public class CryptoManage {

    private static Logger LOG = Logger.getLogger(CryptoManage.class);

    private String iv;
    private String secretKey;
    private String publicKey;
    private String privateKey;
    private String hashMac;

    @Autowired
    private KeysManageDeviceRepository keysManageDeviceRepo;


    public void getIntanceKey(String algorithm) {
        switch (algorithm) {
            case "AES-CBC-128":
                secretKey = RandomStringUtils.random(16, true, true);
                iv = RandomStringUtils.random(16, true, true);
                break;
            case "AES-ECB-128":
                secretKey = RandomStringUtils.random(16, true, true);
                break;
            case "GRAIN":
                secretKey = RandomStringUtils.random(10, true, true);
                iv = RandomStringUtils.random(8, true, true);
            case "XXTEA":
                secretKey = RandomStringUtils.random(16, true, true);
                break;
            default:
                secretKey = "Không tồn tại !";

        }
    }

    public String getCipherText(String plainText, KeysManageDevice keysManageDevice) {
        iv = keysManageDevice.getIv();
        secretKey = keysManageDevice.getSecretKey();
        privateKey = keysManageDevice.getPrivateKey();
        publicKey = keysManageDevice.getPublicKey();
        switch (keysManageDevice.getAlgorithm()) {
            case "AES-ECB-128":
            case "AES-CBC-128":
                return new AesCrypto(secretKey, iv).getCipherText(plainText);
            case "XXTEA":
                return XXTEA.encryptToBase64String(plainText, secretKey);
            default:
                return "";
        }
    }

    public String getPlainText(String cipherText, KeysManageDevice keysManageDevice) {
        iv = keysManageDevice.getIv();
        secretKey = keysManageDevice.getSecretKey();
        privateKey = keysManageDevice.getPrivateKey();
        publicKey = keysManageDevice.getPublicKey();
        switch (keysManageDevice.getAlgorithm()) {
            case "AES-ECB-128":
            case "AES-CBC-128":
                return new AesCrypto(secretKey, iv).getPlainText(cipherText);
            case "XXTEA":
                return XXTEA.decryptBase64StringToString(cipherText, secretKey);
            default:
                return "";
        }
    }

    public String getDataAfterHandlerEnCrypto(String data){
        if (data == null || data.length() < 32) return"";

        String[] dataArray = data.split(";");
        if(dataArray.length != 3){
            LOG.info("Du lieu khong hop le !");
            return "";
        }

        String mac         = dataArray[0];
        String MAC         = dataArray[1];
        String message     = dataArray[2];

        KeysManageDevice keysManageDevice = keysManageDeviceRepo.findByMacAddress(mac);
        if(keysManageDevice == null){
            LOG.info("Khong tim thay Key tuong ung !");
            return "";
        }

        String key       = keysManageDevice.getSecretKey();
        String tempMAC   = convertMd5(message + key);
        if(!MAC.equalsIgnoreCase(tempMAC)){
            LOG.info("Du lieu da bi sua doi !");
            return "";
        }

        return mac + ";" + getPlainText(message, keysManageDevice);
    }

    public String convertMd5(String str){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] digest = md.digest();
            return DatatypeConverter.printHexBinary(digest).toUpperCase();
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
        return "";
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }


    public String getHashMac() {
        return hashMac;
    }

    public void setHashMac(String hashMac) {
        this.hashMac = hashMac;
    }
}
