package com.ecommerce.userservice.util;

import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.ecommerce.userservice.config.ApplicationProperties;
import com.ecommerce.userservice.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.GetPublicKeyRequest;
import software.amazon.awssdk.services.kms.model.GetPublicKeyResponse;
import software.amazon.awssdk.services.kms.model.SignRequest;
import software.amazon.awssdk.services.kms.model.SignResponse;
import software.amazon.awssdk.services.kms.model.SigningAlgorithmSpec;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

@Slf4j
@Component
public class KeyProviderUtil {
    private final KmsClient kmsClient;
    private final ApplicationProperties applicationProperties;
    
    public KeyProviderUtil(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        // Create AWS credentials
        if (List.of(Constant.PROFILE_STAGING, Constant.PROFILE_PRODUCTION).contains(applicationProperties.getActiveProfile())) {
            AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(applicationProperties.getAccessKey(),
                    applicationProperties.getSecretKey());
            this.kmsClient = KmsClient
                    .builder()
                    .region(Region.AP_SOUTHEAST_2)
                    .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                    .build();
        } else {
            this.kmsClient = null;
        }
    }
    
    public RSAPublicKey getPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        if (List.of(Constant.PROFILE_STAGING, Constant.PROFILE_PRODUCTION).contains(
                applicationProperties.getActiveProfile())) {
            try {
                GetPublicKeyRequest publicKeyRequest = GetPublicKeyRequest
                        .builder()
                        .keyId(applicationProperties.getKmsKeyId())
                        .build();
                GetPublicKeyResponse publicKeyResponse = kmsClient.getPublicKey(publicKeyRequest);
                byte[] publicKeyBytes = publicKeyResponse.publicKey().asByteArray();
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                return (RSAPublicKey) keyFactory.generatePublic(keySpec);
            } catch (Exception exception) {
                log.error("Error public key:", exception);
                return null;
            }
        }
        
        return KeyReaderUtil.getPublicKey(CommonUtil.combineString(
                applicationProperties.getKeyLocalPath(), applicationProperties.getJwtSignPublicKey()));
    }
    
    public String generate(JWTCreator.Builder jwt)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        if (List.of(Constant.PROFILE_STAGING, Constant.PROFILE_PRODUCTION)
                .contains(applicationProperties.getActiveProfile())) {
            
            Algorithm algorithm = Algorithm.RSA256(null, null);
            String data = jwt.sign(algorithm);
            String[] parts = data.split("\\.");
            String encodedHeader = parts[0];
            String encodedPayload = parts[1];
            String messageToSign = encodedHeader + "." + encodedPayload;
            
            // Sign the JWT using KMS
            SignRequest signRequest = SignRequest.builder()
                    .keyId(applicationProperties.getKmsKeyId())
                    .message(SdkBytes.fromUtf8String(messageToSign))
                    .signingAlgorithm(SigningAlgorithmSpec.RSASSA_PKCS1_V1_5_SHA_256)
                    .build();
            
            SignResponse signResponse = kmsClient.sign(signRequest);
            byte[] signature = signResponse.signature().asByteArray();
            // Base64URL encode the signature
            String encodedSignature = Base64.getUrlEncoder().withoutPadding().encodeToString(signature);
            
            // Step 4: Combine Header, Payload, and Signature to form the final JWT
            return messageToSign + "." + encodedSignature;
        }
        RSAPrivateKey privateKey = KeyReaderUtil.getPrivateKey(CommonUtil.combineString(
                applicationProperties.getKeyLocalPath(), applicationProperties.getJwtSignPrivateKey()));
        return jwt.sign(Algorithm.RSA256(null, privateKey));
    }
}

