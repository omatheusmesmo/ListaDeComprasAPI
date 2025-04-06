package com.omatheusmesmo.shoppmate.auth.service.service;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;
    private final RSAEncrypter encrypter;
    private final RSADecrypter decrypter;

    @Value("${jwt.token.expiration}")
    private long tokenExpiration;

    public JwtService() {
        KeyPair keyPair = generateRSAKeys();
        this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
        this.publicKey = (RSAPublicKey) keyPair.getPublic();
        this.encrypter = new RSAEncrypter(publicKey);
        this.decrypter = new RSADecrypter(privateKey);
    }

    public String generateToken(UserDetails userDetails) {
        return encryptToken(userDetails).serialize();
    }

    protected EncryptedJWT encryptToken(UserDetails userDetails) {
        try {
            EncryptedJWT encryptedJWT = new EncryptedJWT(buildHeader(), buildToken(userDetails));
            encryptedJWT.encrypt(encrypter);
            return encryptedJWT;
        } catch (JOSEException e) {
            logger.error("Failed to encrypt token", e);
            throw new JwtServiceException("Failed to encrypt token", e);
        }
    }

    public boolean validateToken(String token) {
        try {
            EncryptedJWT encryptedJWT = EncryptedJWT.parse(token);
            encryptedJWT.decrypt(decrypter);
            SignedJWT signedJWT = encryptedJWT.getPayload().toSignedJWT();
            return signedJWT != null && signedJWT.verify(new RSASSAVerifier(publicKey));
        } catch (Exception e) {
            logger.error("Invalid token", e);
            throw new JwtServiceException("Invalid token", e);
        }
    }

    public JWTClaimsSet decryptToken(String token) {
        try {
            EncryptedJWT encryptedJWT = EncryptedJWT.parse(token);
            encryptedJWT.decrypt(decrypter);
            return encryptedJWT.getJWTClaimsSet();
        } catch (Exception e) {
            logger.error("Failed to decrypt token", e);
            throw new JwtServiceException("Failed to decrypt token", e);
        }
    }

    private JWEHeader buildHeader() {
        return new JWEHeader
                .Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A128GCM)
                .build();
    }

    private JWTClaimsSet buildToken(UserDetails userDetails) {
        return new JWTClaimsSet.Builder()
                .subject(userDetails.getUsername())
                .expirationTime(new Date(new Date().getTime() + tokenExpiration))
                .notBeforeTime(new Date())
                .jwtID(UUID.randomUUID().toString())
                .build();
    }

    protected KeyPair generateRSAKeys() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            logger.error("Error generating RSA keys", e);
            throw new JwtServiceException("Error generating RSA keys", e);
        }
    }

}
