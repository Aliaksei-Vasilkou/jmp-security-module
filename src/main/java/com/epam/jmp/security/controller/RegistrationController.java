package com.epam.jmp.security.controller;

import com.epam.jmp.security.model.AppUser;
import com.epam.jmp.security.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<String> register(@RequestBody AppUser request) {
        AppUser user = new AppUser();
        user.setName(request.getName());
        user.setPassword(request.getPassword());
        userService.register(user);

        return ResponseEntity.ok("User successfully registered");
    }

}
