package com.epam.jmp.security.controller;

import com.epam.jmp.security.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class ApplicationController {

    private final UserService userService;

    @GetMapping("/about")
    public String about() {

        return "about";
    }

    @GetMapping("/info")
    public String info() {

        return "info";
    }

    @GetMapping("/admin")
    public String admin() {

        return "admin";
    }

    @GetMapping("/login")
    public String login(ModelMap model, @RequestParam("error") Optional<String> error) {
        error.ifPresent(e -> model.addAttribute("error", e));

        return "login";
    }

    @GetMapping("/loggedOut")
    public String logout() {

        return "logout";
    }

    @GetMapping("/blocked")
    public String blockedUsers(Model model) {
        Map<String, LocalDateTime> blockedUsers = userService.getBlockedUsers();
        if (!blockedUsers.isEmpty()) {
            model.addAttribute("blockedUsers", blockedUsers);
        }

        return "blocked";
    }
}
