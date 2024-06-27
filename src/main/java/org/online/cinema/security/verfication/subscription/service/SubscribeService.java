package org.online.cinema.security.verfication.subscription.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
public class SubscribeService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public String generateSubscriptionToken() {
        return UUID.randomUUID().toString();
    }

    public void saveSubscriptionToken(String email, String token) {
        redisTemplate.opsForValue().set(token, email, Duration.ofHours(24));
    }

    public String getEmailByToken(String token) {
        return redisTemplate.opsForValue().get(token);
    }

    public boolean verifySubscriptionToken(String email, String token) {
        String storedEmail = redisTemplate.opsForValue().get(token);
        return email.equals(storedEmail);
    }
}
