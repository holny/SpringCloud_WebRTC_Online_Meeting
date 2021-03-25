package com.hly.july.test;

import com.hly.july.common.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


/**
 * @author Linyuan Hou
 * @date 2021/3/17 13:07
 */
@RunWith(SpringRunner.class)
public class MyTest {
    public static final String ENCRYPT_ALGORITHM = "RSA";
    private JwtUtils jwtUtils;
    public Claims getClaimsFromToken(String token, PublicKey publicKey) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            throw e;
        }
        return claims;
    }
    @Test
    public void JwtTest() throws CertificateException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String publicKeyFilePath = System.getProperty("user.dir") + "/certificate/july-pubkey_0317_pure.txt";
        Resource resource = new FileSystemResource(publicKeyFilePath);
        String publicKeyString = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()));
//        String jwt_token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjcmVhdGVUaW1lIjoiMjAyMS0wMy0xNyAxMzo1MToyMCIsInVzZXJfbmFtZSI6IlRvbSIsImF1dGhvciI6Imp1bHktYXV0aCIsInNjb3BlIjpbImFsbCJdLCJleHAiOjE2MTYwMDM0ODAsInVzZXJOYW1lIjoiVG9tIiwidXNlcklkIjoxLCJhdXRob3JpdGllcyI6WyJERUxFVEUiLCJVUERBVEUiLCJJTlNFUlQiLCJST0xFX0FETUlOIiwiQ1JFQVRFIiwiUk9MRV9VU0VSIl0sImp0aSI6ImI5MDczMjAyLTg2MzItNDBhOC1iOTE3LTUxNzY4ZDdkOTI4NCIsImNsaWVudF9pZCI6ImNsaWVudCJ9.by-rgz5vfT96JjC9acv473sXCIkAIpB1mEjZPuTL5XZ_2YLKVKFcGytDAWdC3qznN4pUXZKAPsD7sPgn5juAICGLBZEd3dLEYKKokcWiNEetBNxaibhp8qWwVbyEnbaDFCcFHN6Hk14woa_7CMV7sydJalH116BnmsR1VpohsG6ts9CU_r1v7m0IbbZGNbJSoEMPPSajxy63pVZtJylEYPXBBLy0z6mBsCkC8yImMjk1MDINEYdf1crPWTqThUJzYTO14PqAUYGSp3KeP1fnK6WyhJyOes8wxpUClWlBX394IsQgFmO5Igz4cx7k3tsqsFZW9oUs4sCQSV9C7ZEIfQ";
//        byte[] bytes = Base64.getMimeDecoder().decode(publicKeyString.replace("\n", "").replace("\r", "").replace(" ", "").getBytes());
        byte[] bytes = Base64.getMimeDecoder().decode(publicKeyString.replace("-----BEGIN PUBLIC KEY-----","").replace("-----END PUBLIC KEY-----","").replace("\n", "").replace("\r", "").replace(" ", "").getBytes());
        System.out.println("*******************************************");
        System.out.println(new String(bytes,"utf-8"));
//        byte[] bytes = Base64.decodeBase64(publicKeyString.replace("\n", "").replace(" ", "").getBytes());
        X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance(ENCRYPT_ALGORITHM);
        PublicKey publicKey = factory.generatePublic(spec);
//        Claims claims = getClaimsFromToken(jwt_token, publicKey);
//        System.out.println("*******************************************");
//        System.out.println(claims.get("userName"));
//        System.out.println(claims.get("authorities"));
//        System.out.println(claims.get("jti"));

    }


}
