package org.online.cinema.api.client.controller;

import org.online.cinema.data.dto.entity.UserInfoDTO;
import org.online.cinema.data.exceptionhandler.exception.UserInfoException;
import org.online.cinema.store.entity.UserInfo;
import org.online.cinema.store.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api"})
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    public UserInfoController() {
    }

    @GetMapping({"/user/info"})
    public UserInfoDTO getUserInfo() {
        UserInfo userInfo = this.userInfoService.getUserInfo();
        if (userInfo == null) {
            throw new UserInfoException("User info not found");
        }

        else {
            return UserInfoDTO.builder()
                    .first_name(userInfo.getFirstName())
                    .last_name(userInfo.getLastName())
                    .gender(userInfo.getGender())
                    .registration_date(userInfo.getRegistrationDate())
                    .build();
        }
    }

    @PutMapping({"/user/info"})
    public UserInfo updateUserInfo(@RequestBody UserInfoDTO userInfoDTO) {
        UserInfo userInfo = this.userInfoService.getUserInfo();
        if (userInfo == null) {
            throw new RuntimeException("User info not found");
        }
        if (!userInfoDTO.getGender().equals("Male") && !userInfoDTO.getGender().equals("Female")) {
            throw new UserInfoException("User info gender is incorrect");
        } else {
            userInfo.setFirstName(userInfoDTO.getFirst_name());
            userInfo.setLastName(userInfoDTO.getLast_name());
            userInfo.setGender(userInfoDTO.getGender());
            return this.userInfoService.saveInfo(userInfo);
        }
    }
}
