package com.epam.jmp.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final String BAD_CREDENTIALS_ERROR_MESSAGE = "Bad credentials";
    private static final String USER_BLOCKED_ERROR_MESSAGE = "User is blocked";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        super.setDefaultFailureUrl("/login?error");
        super.onAuthenticationFailure(request, response, exception);
        String errorMessage = BAD_CREDENTIALS_ERROR_MESSAGE;

        if (exception.getMessage().equalsIgnoreCase(USER_BLOCKED_ERROR_MESSAGE)) {
            errorMessage = USER_BLOCKED_ERROR_MESSAGE;
        }

        request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, errorMessage);
    }
}
