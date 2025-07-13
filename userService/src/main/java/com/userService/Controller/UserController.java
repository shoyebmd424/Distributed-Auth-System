package com.userService.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/public")
    public String publicEndpoint() {
        return "Public endpoint - no auth needed";
    }

    @GetMapping("/admin")
    public String adminEndpoint() {
        return "Admin endpoint";
    }

    @GetMapping("/user")
    public String userEndpoint() {
        return "User endpoint";
    }
}
