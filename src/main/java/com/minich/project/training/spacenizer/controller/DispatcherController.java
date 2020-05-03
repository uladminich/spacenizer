package com.minich.project.training.spacenizer.controller;

import com.minich.project.training.spacenizer.core.server.ConnectionManager;
import com.minich.project.training.spacenizer.core.service.MockProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DispatcherController {

    @Autowired
    private ConnectionManager connectionManager;

    @Autowired
    private MockProvider mockProvider;

    @RequestMapping(value = {"/board"}, method = RequestMethod.GET)
    public String toBoard(@RequestParam String id) {
        if (!connectionManager.getGameTokens().contains(id) && !mockProvider.isMockId(id)) {
            return "redirect:error";
        }
        return "board";
    }

    @RequestMapping(value = {"/", "/lobby"}, method = RequestMethod.GET)
    public String toHome() {
        return "lobby";
    }

    @RequestMapping(value = {"/error"}, method = RequestMethod.GET)
    public String toError() {
        return "error";
    }
}
