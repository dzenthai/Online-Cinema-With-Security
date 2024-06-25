package org.online.cinema.api.security.controller;

import jakarta.mail.MessagingException;
import lombok.SneakyThrows;
import org.online.cinema.api.security.verfication.service.EmailService;
import org.online.cinema.data.dto.entity.UserDTO;
import org.online.cinema.data.exceptionhandler.exception.UserAlreadyExistException;
import org.online.cinema.api.security.verfication.service.TempUserService;
import org.online.cinema.store.entity.User;
import org.online.cinema.store.entity.UserInfo;
import org.online.cinema.store.service.UserInfoService;
import org.online.cinema.store.service.UserService;
import org.online.cinema.api.security.verfication.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import static java.time.LocalDate.*;

@RestController
@RequestMapping("/api/auth")
public class RegistrationController {

    @Autowired
    private TempUserService tempUserService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/registration")
    public String registerUser(@RequestBody UserDTO user) throws MessagingException {

        String email = user.getEmail();

        if (userService.findByEmail(email) != null) {
            throw new UserAlreadyExistException("User with email - %s, already exist.".formatted(email));
        } else {

        String code = verificationService.generateVerificationCode();

        User tempUser = User.builder()
                .email(email)
                .password(user.getPassword())
                .role("ROLE_USER")
                .enabled(false)
                .build();

            tempUserService.saveTempUser(email, tempUser);
            emailService.sendVerificationEmail(email, code);
            verificationService.saveVerificationCode(email, code);

            return "Registration successful. Check your email for the verification code.";
        }
    }


    @SneakyThrows
    @PostMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestBody Map<String, String> request) {
        if (!request.containsKey("email") || !request.containsKey("code")) {
            return ResponseEntity.badRequest().body("Email and code must be provided.");
        }

        String email = request.get("email");
        String code = request.get("code");

        boolean isValid = verificationService.verifyCode(email, code);
        if (isValid) {
            User user = tempUserService.getTempUser(email);
            if (user != null) {
                userService.registerUser(user);
                LocalDate localDate = LocalDate.now();
                UserInfo userInfo = UserInfo.builder()
                        .user(user)
                        .isSubscribed(false)
                        .registrationDate(Date.from(localDate.atStartOfDay
                                (ZoneId.systemDefault()).toInstant()))
                        .build();
                userInfoService.saveInfo(userInfo);
                tempUserService.removeTempUser(email);
                return ResponseEntity.ok("Email verified successfully.");
            } else {
                return ResponseEntity.badRequest().body("No user data found for the provided email.");
            }
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired verification code.");
        }
    }
}
