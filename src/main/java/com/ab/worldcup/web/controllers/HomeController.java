package com.ab.worldcup.web.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String home() {
        return "index.html";
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/admin")
    public String adminHome() {
        return "/admin/index.html";
    }

}
