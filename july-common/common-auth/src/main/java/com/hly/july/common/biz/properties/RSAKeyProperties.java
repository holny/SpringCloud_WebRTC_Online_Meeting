package com.hly.july.common.biz.properties;

import com.hly.july.common.biz.util.RSAUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.PublicKey;

/**
 * @author Linyuan Hou
 * @date 2021/3/16 21:53
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "rsa.key")
public class RSAKeyProperties {
    private String publicKeyFilePath = "";
    private PublicKey publicKey = null;
    private String publicKeyString = null;

    @PostConstruct
    public void createRSAKey() throws Exception {
        this.publicKeyFilePath = System.getProperty("user.dir") + this.publicKeyFilePath;
        Resource resource = new FileSystemResource(this.publicKeyFilePath);
        log.info("linyhou publicKeyFilePath:{}",this.publicKeyFilePath);
        try {
            this.publicKeyString = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()));
            log.info("linyhou publicKeyString:{}",this.publicKeyString);
            String base64String = this.publicKeyString .replace("-----BEGIN PUBLIC KEY-----","").replace("-----END PUBLIC KEY-----","").replace("\n", "").replace("\r", "").replace(" ", "");
            log.info("linyhou base64String:{}",base64String);
            this.publicKey = RSAUtils.getPublicKeyByKeyBytes(base64String.getBytes());
            log.info("linyhou publicKey:{}",this.publicKey.toString());
        } catch (IOException e) {
            throw new RuntimeException("获取jwt公钥信息失败，exception:"+e.getMessage());
        }
        this.publicKeyString = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()));
        log.info("linyhou publicKeyString:{}",this.publicKeyString);
//        this.publicKey = RSAUtils.getPublicKeyByKeyString(this.publicKeyString);
//        log.info("linyhou publicKey:{}",this.publicKey.toString());
    }

}
