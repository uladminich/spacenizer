package com.minich.project.training.spacenizer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DispatcherController {

    @RequestMapping(value = {"/", "/alpha"}, method = RequestMethod.GET)
    public String toHome() {
        return "alpha";
    }

    @RequestMapping(value = {"/board"}, method = RequestMethod.GET)
    public String toBoad() {
        return "board";
    }

    @RequestMapping(value = "/test-error", method = RequestMethod.GET)
    public String testError() {
        throw new RuntimeException("Test error page");
    }
}
