package org.online.cinema.api.security.verfication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;
import java.util.UUID;

@Service
public class VerificationService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final int CODE_LENGTH = 6;

    public String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    public void saveVerificationCode(String email, String code) {
        redisTemplate.opsForValue().set(email, code, Duration.ofMinutes(10));
    }

    public boolean verifyCode(String email, String code) {
        String storedCode = redisTemplate.opsForValue().get(email);
        return code.equals(storedCode);
    }
}
