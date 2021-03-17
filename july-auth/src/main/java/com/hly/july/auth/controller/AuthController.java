package com.hly.july.auth.controller;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;

import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * @author Linyuan Hou
 * @date 2021/3/16 10:35
 */
@Slf4j
@RestController
public class AuthController {
    @Autowired
    private KeyPair keyPair;

    @GetMapping("/getPublicKey")
    public Map<String, Object> getPublicKey() {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        JSONObject jsonObject = new JWKSet(key).toJSONObject();
        log.info("hly getPublicKey:{}",jsonObject.toJSONString());
        return jsonObject;
    }
}
