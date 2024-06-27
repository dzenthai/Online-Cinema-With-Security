package org.online.cinema.store.service;

import java.util.Optional;

import org.online.cinema.store.entity.User;
import org.online.cinema.store.entity.UserInfo;
import org.online.cinema.store.repo.UserInfoRepository;
import org.online.cinema.store.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

    public UserInfo saveInfo(UserInfo userInfo) {
        if (userInfo.isSubscribed()) {
            User user = null;
            String email = this.contextHolder.getCurrentEmail();
            Optional<User> optional = this.userRepository.findByEmail(email);
            if (optional.isPresent()) {
                user = optional.get();
                this.userInfoRepository.findUserByUser(user);
                user.setRole(user.getRole() + ", SUBSCRIBED");
            } else {
                throw new UsernameNotFoundException("User not found");
            }
        }
        return this.userInfoRepository.save(userInfo);
    }

    public UserInfo getUserInfo() {
        String email = this.contextHolder.getCurrentEmail();
        Optional<User> user = this.userRepository.findByEmail(email);
        if (user.isPresent()) {
            return this.userInfoRepository.findUserByUser(user.get());
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

//    public UserInfo addSubscriptionToUser(UserInfo userInfo) {
//        String email = this.contextHolder.getCurrentEmail();
//        User user = null;
//        Optional<User> optional = this.userRepository.findByEmail(email);
//        if (optional.isPresent()) {
//            user = optional.get();
//            userInfo = this.userInfoRepository.findUserByUser(optional.get());
//            userInfo.setSubscribed(true);
//            user.setRole(user.getRole() + ", SUBSCRIBED");
//            return this.userInfoRepository.save(userInfo);
//        } else {
//            throw new UsernameNotFoundException("User not found");
//        }
//    }

    public UserInfo getUserInfoByEmail(String email) {
        return userInfoRepository.findByUser_Email(email);
    }
}
