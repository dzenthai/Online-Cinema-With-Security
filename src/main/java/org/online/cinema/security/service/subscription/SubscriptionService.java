package org.online.cinema.security.service.subscription;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Service
public class SubscriptionService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public String generateSubscriptionToken() {
        return UUID.randomUUID().toString();
    }

    public void saveSubscriptionToken(String email, String token) {
        log.info("Saving the subscription token: email={}", email);
        redisTemplate.opsForValue().set(token, email, Duration.ofHours(24));
    }

    public String getEmailByToken(String token) {
        return redisTemplate.opsForValue().get(token);
    }

    public boolean verifySubscriptionToken(String email, String token) {
        String storedEmail = redisTemplate.opsForValue().get(token);
        return email.equals(storedEmail);
    }

    public void deleteSubscriptionToken(String email) {
        log.warn("Clearing the subscription token from the database: email={}", email);
        redisTemplate.delete(email);
    }
}
