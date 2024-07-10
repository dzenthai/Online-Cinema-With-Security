package org.online.cinema.user.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.online.cinema.user.entity.User;
import org.online.cinema.user.entity.UserInfo;
import org.online.cinema.user.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Lazy
    @Autowired
    private UserInfoService userInfoService;

    public UserService() {
    }

    @Transactional
    public User findByEmail(String email) {
        User user = null;
        Optional<User> optionalUser = this.userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        }
        log.info("Getting user by email: email={}", email);
        return user;
    }

    @Transactional
    public void registerUser(User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        this.userRepository.save(user);

        String username = user.getEmail().split("@")[0] + "-" + UUID.randomUUID().toString().substring(0,16);

        LocalDate localDate = LocalDate.now();
        UserInfo userInfo = UserInfo.builder().user(user)
                .username(username)
                .gender("Not specified")
                .isSubscribed(false).
                registrationDate(Date.from(localDate
                        .atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .build();
        this.userInfoService.saveInfo(userInfo);
        log.info("Saving user: email={}, username={}", user.getEmail(), userInfo.getUsername());
    }
}
