package org.online.cinema.user.service;

import lombok.extern.slf4j.Slf4j;
import org.online.cinema.common.exception.UserException;
import org.online.cinema.security.service.ContextHolderService;
import org.online.cinema.user.entity.User;
import org.online.cinema.user.entity.UserInfo;
import org.online.cinema.user.repo.UserInfoRepository;
import org.online.cinema.user.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class UserInfoService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private ContextHolderService contextHolder;

    public UserInfoService() {
    }

    @Transactional
    public UserInfo saveInfo(UserInfo userInfo) {
        if (userInfo.isSubscribed()) {
            User user = null;
            String email = this.contextHolder.getCurrentEmail();
            Optional<User> optional = this.userRepository.findByEmail(email);
            if (optional.isPresent()) {
                user = optional.get();
                this.userInfoRepository.findUserByUser(user);
            } else {
                throw new UserException("User not found");
            }
            if (user.getRole().contains("SUBSCRIBED")) {
                throw new UserException("User %s is already subscribed".formatted(email));
            } else {
                user.setRole(user.getRole() + ", SUBSCRIBED");
                log.info("Setting and saving role for user: email={}, role(s)={}",
                        email, user.getRole());
                this.userRepository.save(user);
            }
        }
        log.info("Saving user info: email={}", userInfo.getUser().getEmail());
        return this.userInfoRepository.save(userInfo);
    }

    @Transactional
    public UserInfo getUserInfo() {
        String email = this.contextHolder.getCurrentEmail();
        Optional<User> user = this.userRepository.findByEmail(email);
        if (user.isPresent()) {
            log.info("Getting user from database: email={}", email);
            return this.userInfoRepository.findUserByUser(user.get());
        } else {
            throw new UserException("User not found");
        }
    }

    @Transactional
    public UserInfo getUserInfoByEmail(String email) {
        return userInfoRepository.findByUser_Email(email);
    }
}
