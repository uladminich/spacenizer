package com.minich.project.training.sudoku.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DispatcherController {

    @RequestMapping(value = {"/", "/welcome"}, method = RequestMethod.GET)
    public String index() {
        return "welcome";
    }

    @RequestMapping(value = "/test-error", method = RequestMethod.GET)
    public String testError() {
        throw new RuntimeException("Test error page");
    }
}
