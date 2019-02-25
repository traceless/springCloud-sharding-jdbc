package com.phantoms.framework.cloudbase.util;


import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.Key;

import org.springframework.security.crypto.codec.Base64;
public class RSAHandle {

    static String publicKey;
    static String privateKey;

    static {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(RSAUtils.PUBLIC_KEY_FILE));
            Key key = (Key) ois.readObject();
            publicKey = new String(Base64.encode(key.getEncoded()));
            ObjectInputStream ois2 = new ObjectInputStream(new FileInputStream(RSAUtils.PRIVATE_KEY_FILE));
            Key key2 = (Key) ois2.readObject();
            privateKey = new String(Base64.encode(key2.getEncoded()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws Exception {
        System.err.println("公钥加密——私钥解密");
        String source = "MVR-0hme26RnGcLy75PB5bqh24pmcmC4vipGK-1Dwv47h7Xv6QeM0YmASV6q4Z5opEGk7po_DUjlJwE_sqhrm6LabLPooZgtuEMohqEAHUGkgofZz0Ew9G6maSrNlLVpMUVeAHDEMN";
        System.out.println("\r加密前文字：\r\n" + source);
        byte[] data = source.getBytes();
        byte[] encodedData = RSAUtils.encryptByPublicKey(data, publicKey);
        System.out.println("加密后文字：\r\n" + new String(encodedData));
        byte[] decodedData = RSAUtils.decryptByPrivateKey(encodedData, privateKey);
        String target = new String(decodedData);
        System.out.println("解密后文字: \r\n" + target);
    }

    public static String RsaDecode(byte[] encodedData) throws Exception {
        ObjectInputStream ois2 = new ObjectInputStream(new FileInputStream(RSAUtils.PRIVATE_KEY_FILE));
        Key key2 = (Key) ois2.readObject();
        String privateKey = new String(Base64.encode(key2.getEncoded()));
        byte[] decodedData = RSAUtils.decryptByPrivateKey(encodedData, privateKey);
        String target = new String(decodedData);
        System.out.println("解密后文字: \r\n" + target);
        return target;
    }
}

