package com.epam.jmp.security.listener;

import com.epam.jmp.security.repository.UserRepository;
import com.epam.jmp.security.service.LoginAttemptService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        Object principal = event.getAuthentication().getPrincipal();

        if (principal instanceof String username && userRepository.findUserByName(username) != null) {
            loginAttemptService.loginFailed(username);
        }
    }
}
