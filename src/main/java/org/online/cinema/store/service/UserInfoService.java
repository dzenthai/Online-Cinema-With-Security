//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

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
        return (UserInfo)this.userInfoRepository.save(userInfo);
    }

    public UserInfo getUserInfo() {
        String email = this.contextHolder.getCurrentEmail();
        Optional<User> user = this.userRepository.findByEmail(email);
        if (user.isPresent()) {
            return this.userInfoRepository.findUserByUser((User)user.get());
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
