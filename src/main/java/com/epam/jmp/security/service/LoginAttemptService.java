package com.epam.jmp.security.service;

import com.epam.jmp.security.model.CachedValue;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {

    private static final int BLOCK_DURATION = 5;
    private static final int MAX_ATTEMPTS = 3;
    private final LoadingCache<String, CachedValue> loginAttemptCache;

    public LoginAttemptService() {
        loginAttemptCache = CacheBuilder.newBuilder()
                .expireAfterWrite(BLOCK_DURATION, TimeUnit.MINUTES)
                .build(new CacheLoader<>() {
                    @Override
                    public CachedValue load(String key) {
                        return new CachedValue(0, LocalDateTime.now());
                    }
                });
    }

    public void loginFailed(String username) {
        CachedValue cachedValue = new CachedValue();
        try {
            cachedValue = loginAttemptCache.get(username);
            cachedValue.setAttempts(cachedValue.getAttempts() + 1);
        } catch (ExecutionException e) {
            cachedValue.setAttempts(0);
        }

        if (isBlocked(username) && cachedValue.getBlockedTimestamp() == null) {
            cachedValue.setBlockedTimestamp(LocalDateTime.now());
        }

        loginAttemptCache.put(username, cachedValue);
    }

    public boolean isBlocked(String username) {
        try {
            return loginAttemptCache.get(username).getAttempts() >= MAX_ATTEMPTS;
        } catch (ExecutionException e) {
            return false;
        }
    }

    public CachedValue getCachedValue(String username) {
        try {
            return loginAttemptCache.get(username);
        } catch (ExecutionException e) {
            return null;
        }
    }
}
