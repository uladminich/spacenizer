package com.minich.project.training.spacenizer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DispatcherController {

    @RequestMapping(value = {"/board"}, method = RequestMethod.GET)
    public String toBoard() {
        return "board";
    }

    @RequestMapping(value = {"/board-mock-design"}, method = RequestMethod.GET)
    public String toMockBoardDesign() {
        return "board-mock-design";
    }

    @RequestMapping(value = {"/", "/lobby"}, method = RequestMethod.GET)
    public String toHome() {
        return "lobby";
    }

    @RequestMapping(value = {"/test"}, method = RequestMethod.GET)
    public String toTestPage() {
        return "test";
    }

    @RequestMapping(value = "/test-error", method = RequestMethod.GET)
    public String testError() {
        throw new RuntimeException("Test error page");
    }
}
