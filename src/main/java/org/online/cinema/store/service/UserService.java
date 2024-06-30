package org.online.cinema.store.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import org.online.cinema.store.entity.User;
import org.online.cinema.store.entity.UserInfo;
import org.online.cinema.store.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            user = (User)optionalUser.get();
        }

        return user;
    }

    @Transactional
    public User registerUser(User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        this.userRepository.save(user);
        LocalDate localDate = LocalDate.now();
        UserInfo userInfo = UserInfo.builder().user(user).isSubscribed(false).registrationDate(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())).build();
        this.userInfoService.saveInfo(userInfo);
        return user;
    }
}
