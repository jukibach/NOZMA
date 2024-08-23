package com.ecommerce.userservice.util;

import com.ecommerce.userservice.constant.Constant;
import org.apache.commons.io.IOUtils;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyReaderUtil {
    private KeyReaderUtil() {
    }
    
    public static RSAPrivateKey getPrivateKey(String fileName) throws IOException, NoSuchAlgorithmException,
            InvalidKeySpecException {
        URL resource = ResourceUtils.getURL(fileName);
        InputStream inputStream = resource.openStream();
        String key = new String(IOUtils.toByteArray(inputStream));
        
        // Remove the PEM header, footer, and whitespace
        key = key.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        
        byte[] keyBytes = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(Constant.KEY_RSA_ALGORITHM);
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }
    
    public static RSAPublicKey getPublicKey(String fileName) throws IOException, NoSuchAlgorithmException,
            InvalidKeySpecException {
        URL resource = ResourceUtils.getURL(fileName);
        InputStream inputStream = resource.openStream();
        String key = new String(IOUtils.toByteArray(inputStream));
        
        key = key.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        
        byte[] keyBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(Constant.KEY_RSA_ALGORITHM);
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }
}
