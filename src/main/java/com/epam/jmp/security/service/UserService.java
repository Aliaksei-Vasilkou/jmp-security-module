package com.epam.jmp.security.service;

import com.epam.jmp.security.model.AppUser;
import com.epam.jmp.security.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private static final String DEFAULT_USER_ROLE = "VIEW_INFO";
    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;
    private final PasswordEncoder passwordEncoder;

    public void register(AppUser user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setAuthorities(DEFAULT_USER_ROLE);
        userRepository.save(user);
    }

    public Map<String, LocalDateTime> getBlockedUsers() {
        List<AppUser> users = userRepository.findAll();

        return users.stream()
                .map(AppUser::getName)
                .filter(loginAttemptService::isBlocked)
                .collect(Collectors.toMap(user -> user,
                        user -> loginAttemptService.getCachedValue(user).getBlockedTimestamp()));

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AppUser user = userRepository.findUserByName(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found by name " + username);
        } else {
            if (loginAttemptService.isBlocked(user.getName())) {
                throw new LockedException("User is blocked");
            }
        }

        return User.withUsername(user.getName())
                .password(user.getPassword())
                .authorities(user.getAuthorities().split("\\|"))
                .build();
    }
}
