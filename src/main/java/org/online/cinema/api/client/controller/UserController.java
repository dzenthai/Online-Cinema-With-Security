package org.online.cinema.api.client.controller;

import jakarta.mail.MessagingException;
import org.online.cinema.data.dto.entity.UserInfoDTO;
import org.online.cinema.data.exceptionhandler.exception.UserInfoException;
import org.online.cinema.security.verfication.subscription.service.EmailSender;
import org.online.cinema.security.verfication.subscription.service.SubscribeService;
import org.online.cinema.store.entity.User;
import org.online.cinema.store.entity.UserInfo;
import org.online.cinema.store.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api"})
public class UserController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private SubscribeService subscribeService;

    @Autowired
    private EmailSender emailSender;

    @GetMapping({"/user/info"})
    public UserInfoDTO getUserInfo() {
        UserInfo userInfo = userInfoService.getUserInfo();
        if (userInfo == null) {
            throw new UserInfoException("User info not found");
        } else {
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
        UserInfo userInfo = userInfoService.getUserInfo();
        if (userInfo == null) {
            throw new UserInfoException("User info not found");
        }
        if (!userInfoDTO.getGender().equals("Male") && !userInfoDTO.getGender().equals("Female")) {
            throw new UserInfoException("User gender is incorrect");
        } else {
            userInfo.setFirstName(userInfoDTO.getFirst_name());
            userInfo.setLastName(userInfoDTO.getLast_name());
            userInfo.setGender(userInfoDTO.getGender());
            return userInfoService.saveInfo(userInfo);
        }
    }

    @PutMapping({"/user/subscribe"})
    public String sendSubscriptionEmail() throws MessagingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        UserInfo userInfo = userInfoService.getUserInfoByEmail(email);
        if (userInfo == null) {
            throw new UserInfoException("User info not found for email: " + email);
        }
        String token = subscribeService.generateSubscriptionToken();
        subscribeService.saveSubscriptionToken(email, token);
        String subscriptionLink = "http://localhost:8080/api/user/confirm-subscription?token=" + token;
        emailSender.sendSubscriptionEmail(email, subscriptionLink);
        return "Subscription email sent. Check your email to confirm the subscription.";
    }

    @GetMapping({"/user/confirm-subscription"})
    public String confirmSubscription(@RequestParam("token") String token) {
        String email = subscribeService.getEmailByToken(token);
        if (subscribeService.verifySubscriptionToken(email, token)) {
            UserInfo userInfo = userInfoService.getUserInfoByEmail(email);
            userInfo.setSubscribed(true);
            userInfoService.saveInfo(userInfo);
            return "Subscription confirmed.";
        } else {
            return "Invalid or expired token.";
        }
    }
}
