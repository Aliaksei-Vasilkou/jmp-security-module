package com.epam.jmp.security.controller;

import com.epam.jmp.security.model.AppUser;
import com.epam.jmp.security.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    @ResponseBody
    public ResponseEntity<String> register(@RequestParam String name, @RequestParam String password) {
        AppUser user = new AppUser();
        user.setName(name);
        user.setPassword(password);
        userService.register(user);

        return ResponseEntity.ok("User successfully registered");
    }

}
