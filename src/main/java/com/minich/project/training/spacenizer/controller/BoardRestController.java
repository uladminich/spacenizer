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

    @Autowired
    private ConnectionManager connectionManager;

    @RequestMapping(value = "/api/v1/spacenizer/board", method = RequestMethod.POST)
    public ResponseEntity<String> createBoard(@RequestParam String login, @RequestParam String pass) {
        if (!"minich".equals(login) || !"u".equals(pass)) {
            return ResponseEntity.ok("{\"error\":\"error\"}");
        }
        byte[] loginPassAsBytes = login.concat(pass).concat(String.valueOf(new Random().nextInt(500))).getBytes();
        String token = DigestUtils.md5DigestAsHex(loginPassAsBytes);
        if (connectionManager.getGameTokens().contains(token)) {
            // generate another token TODO
            log.debug("Token already exists.");
        }
        Set<String> ids = connectionManager.getGameTokens();
        //synchronized (ids) {
            ids.add(token);
            connectionManager.setGameTokens(ids);
        //}
        return ResponseEntity.ok("{\"id\":\"" + token + "\"}");
    }
}
