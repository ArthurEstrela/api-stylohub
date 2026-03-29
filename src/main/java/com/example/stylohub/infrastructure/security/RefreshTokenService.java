package com.example.stylohub.infrastructure.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private static final String PREFIX = "refresh_token:";
    private final StringRedisTemplate redis;
    private final long refreshExpirationMs;

    public RefreshTokenService(
            StringRedisTemplate redis,
            @Value("${app.jwt.refresh-expiration-ms}") long refreshExpirationMs) {
        this.redis = redis;
        this.refreshExpirationMs = refreshExpirationMs;
    }

    public String generate(String userId) {
        String token = UUID.randomUUID().toString();
        redis.opsForValue().set(PREFIX + token, userId, Duration.ofMillis(refreshExpirationMs));
        return token;
    }

    public Optional<String> validate(String token) {
        return Optional.ofNullable(redis.opsForValue().get(PREFIX + token));
    }

    /** Deletes the old token and issues a new one (rotation prevents replay attacks). */
    public String rotate(String oldToken, String userId) {
        redis.delete(PREFIX + oldToken);
        return generate(userId);
    }

    public void revoke(String token) {
        redis.delete(PREFIX + token);
    }

    public long getRefreshExpirationMs() {
        return refreshExpirationMs;
    }
}
