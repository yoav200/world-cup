package com.ab.worldcup.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String home() {
        return "index.html";
    }


    @RequestMapping("/api/heartbeat")
    @ResponseBody
    public String heartbeat() {
        return "index.html";
    }


}
