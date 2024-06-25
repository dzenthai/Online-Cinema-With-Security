package org.online.cinema.store.service;

import org.online.cinema.store.entity.User;
import org.online.cinema.store.entity.UserInfo;
import org.online.cinema.store.repo.UserInfoRepository;
import org.online.cinema.store.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    public UserInfo saveInfo(UserInfo userInfo) {

        String email = getCurrentEmail();

        User user = new User();

        Optional<User> optional = userRepository.findByEmail(email);
        if (optional.isPresent()) {
            user = optional.get();
        }

        userInfo.setUser(user);

        return userInfoRepository.save(userInfo);
    }

    private String getCurrentEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}
