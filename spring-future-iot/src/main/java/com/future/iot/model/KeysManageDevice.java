package com.future.iot.model;

import javax.persistence.*;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Entity
@Table(name = "keys_manage_device", uniqueConstraints = @UniqueConstraint(columnNames = {"id"}))
public class KeysManageDevice {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "algo")
    private String algorithm;

    @Column(name = "secret_key")
    private String secretKey;

    @Column(name = "public_key")
    private String publicKey;

    @Column(name = "public_key_client")
    private String publicKeyClient;

    @Column(name = "private_key")
    private String privateKey;

    @Column(name = "mac_address")
    private String macAddress;

    @Column(name = "iv")
    private String iv;

    @Column(name = "hash_mac")
    private String hashMac;


    public String getHashMac() {
        return hashMac;
    }

    public void setHashMac(String hashMac) {
        this.hashMac = hashMac;
    }

    public long getId() {
        return id;
    }


    public String getAlgorithm() {
        return algorithm;
    }


    public String getSecretKey() {
        return secretKey;
    }


    public String getPublicKey() {
        return publicKey;
    }


    public String getPrivateKey() {
        return privateKey;
    }


    public String getMacAddress() {
        return macAddress;
    }

    public String getPublicKeyClient() {
        return publicKeyClient;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(macAddress.getBytes());
            byte[] digest = md.digest();
            this.hashMac = DatatypeConverter.printHexBinary(digest).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public void setPublicKeyClient(String publicKeyClient) {
        this.publicKeyClient = publicKeyClient;
    }
}
