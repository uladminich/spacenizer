package com.minich.project.training.spacenizer.controller;

import com.minich.project.training.spacenizer.core.server.ServerWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@RestController
public class BoardRestController {

    @Autowired
    private ServerWebSocket serverWebSocket;

    @RequestMapping(value = "/api/v1/spacenizer/board", method = RequestMethod.POST)
    public ResponseEntity<String> createBoard(@RequestParam String login, @RequestParam String pass) {
        byte[] loginPassAsBytes = login.concat(pass).getBytes();
        String token = DigestUtils.md5DigestAsHex(loginPassAsBytes);
        if (serverWebSocket.getGameTokens().contains(token)) {
            // generate another token TODO
            log.debug("Token already exists.");
        }
        CopyOnWriteArraySet<String> ids = serverWebSocket.getGameTokens();
        synchronized (ids) {
            ids.add(token);
            serverWebSocket.setGameTokens(ids);
        }
        return ResponseEntity.ok("{\"id\":\"" + token + "\"}");
    }
}
