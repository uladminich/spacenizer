package com.minich.project.training.spacenizer.controller;

import com.minich.project.training.spacenizer.core.server.ConnectionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.Set;

@Slf4j
@RestController
public class BoardRestController {

    private static final String MD5_LOGIN_PASS_EQ = "6c9ef91f381220ef1c83118971caa1b2";
    @Autowired
    private ConnectionManager connectionManager;

    @RequestMapping(value = "/api/v1/spacenizer/board", method = RequestMethod.POST)
    public ResponseEntity<String> createBoard(@RequestParam String login, @RequestParam String pass) {
        String finalCred = login.concat("_").concat(pass);
        if (!MD5_LOGIN_PASS_EQ.equals(DigestUtils.md5DigestAsHex(finalCred.getBytes()))) {
            return ResponseEntity.ok("{\"error\":\"error\"}");
        }
        byte[] loginPassAsBytes = finalCred.concat(String.valueOf(new Random().nextInt(5000))).getBytes();
        String token = DigestUtils.md5DigestAsHex(loginPassAsBytes);
        if (connectionManager.getGameTokens().contains(token)) {
            // generate another token TODO
            log.info("Token already exists.");
            return ResponseEntity.ok("{\"error\":\"error\"}");
        }
        Set<String> ids = connectionManager.getGameTokens();
        ids.add(token);
        connectionManager.setGameTokens(ids);
        return ResponseEntity.ok("{\"id\":\"" + token + "\"}");
    }
}
