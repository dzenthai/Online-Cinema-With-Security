package org.online.cinema.user.controller;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.online.cinema.common.dto.UserInfoDTO;
import org.online.cinema.common.exception.UserInfoException;
import org.online.cinema.security.service.subscription.SubscriptionEmailService;
import org.online.cinema.security.service.subscription.SubscriptionService;
import org.online.cinema.user.entity.UserInfo;
import org.online.cinema.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@RestController
@RequestMapping({"/api"})
public class UserController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private SubscriptionEmailService subscriptionEmailService;

    @GetMapping({"/user/info"})
    public UserInfoDTO getUserInfo() {
        UserInfo userInfo = userInfoService.getUserInfo();
        if (userInfo == null) {
            throw new UserInfoException("User info not found");
        } else {
            log.info("Getting all information about user: email:{}", userInfo.getUser().getEmail());
            return UserInfoDTO.builder()
                    .first_name(userInfo.getFirstName())
                    .last_name(userInfo.getLastName())
                    .gender(userInfo.getGender())
                    .registration_date(userInfo.getRegistrationDate())
                    .is_subscribed(userInfo.isSubscribed())
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
            throw new UserInfoException("Invalid gender selection. Please choose either 'Male' or 'Female'");
        } else {
            userInfo.setFirstName(userInfoDTO.getFirst_name());
            userInfo.setLastName(userInfoDTO.getLast_name());
            userInfo.setGender(userInfoDTO.getGender());
            log.info("Updating information about user: email:{}", userInfo.getUser().getEmail());
            return userInfoService.saveInfo(userInfo);
        }
    }

    @PutMapping({"/user/subscription"})
    public String sendSubscriptionEmail() throws MessagingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        UserInfo userInfo = userInfoService.getUserInfoByEmail(email);
        if (userInfo.isSubscribed()) {
            throw new UserInfoException("User %s is already subscribed".formatted(email));
        } else {
            if (userInfo == null) {
                throw new UserInfoException("User with email %s not found in database".formatted(email));
            }

            String token = subscriptionService.generateSubscriptionToken();

            subscriptionService.saveSubscriptionToken(email, token);
            String subscriptionLink = "http://localhost:8080/api/user/confirm-subscription?token=" + token;
            subscriptionEmailService.sendSubscriptionEmail(email, subscriptionLink);

            log.info("Subscription email sent: email={}, status={}", email, HttpStatus.OK.getReasonPhrase());
            return "Subscription email sent. Check your email - %s to confirm the subscription.".formatted(email);
        }
    }

    @GetMapping({"/user/confirm-subscription"})
    public RedirectView confirmSubscription(@RequestParam("token") String token) {
        String email = subscriptionService.getEmailByToken(token);
        if (subscriptionService.verifySubscriptionToken(email, token)) {
            UserInfo userInfo = userInfoService.getUserInfoByEmail(email);
            userInfo.setSubscribed(true);
            userInfoService.saveInfo(userInfo);
            subscriptionService.deleteSubscriptionToken(token);
            log.info("Successful subscription confirmation: status={}." +
                    "User {} has successfully subscribed and is being redirected via GET request to /api/user/info. ",
                    HttpStatus.TEMPORARY_REDIRECT.getReasonPhrase(), email);
            return new RedirectView("/api/user/info");
        } else {
            throw new UserInfoException("Invalid or expired token.");
        }
    }
}
