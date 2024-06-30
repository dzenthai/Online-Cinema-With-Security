package org.online.cinema.security.controller;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.SneakyThrows;
import org.online.cinema.security.verfication.registration.service.VerificationEmailService;
import org.online.cinema.security.service.TempUserService;
import org.online.cinema.security.verfication.registration.service.VerificationService;
import org.online.cinema.data.dto.entity.UserDTO;
import org.online.cinema.data.exception.UserException;
import org.online.cinema.store.entity.User;
import org.online.cinema.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
public class RegistrationController {

    @Autowired
    private TempUserService tempUserService;

    @Autowired
    private VerificationEmailService verificationEmailService;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private UserService userService;

    @PostMapping("/registration")
    public String registerUser(@Valid @RequestBody UserDTO user, BindingResult bindingResult) throws MessagingException {

        String email = user.getEmail();

        /**
         * Validation adding
         */

        if (userService.findByEmail(email) != null) {
            throw new UserException("User with email - %s, already exist.".formatted(email));

        }
        if (!email.matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]" +
                "+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")) {
            throw new ValidationException("Email should be in the format 'your_email@gmail.com'," +
                    " where 'something' can contain any characters and 'domain' should not contain spaces.");
        }
        if (user.getPassword().length() < 8) {
            throw new ValidationException("Password length must be at least 8 characters.");
        }
        if (!user.getPassword().matches(".*\\d.*")) {
            throw new ValidationException("Password must contain at least one digit.");
        }
        if (!user.getPassword().matches(".*[a-z].*")) {
            throw new ValidationException("Password must contain at least one lowercase letter.");
        }
        if (!user.getPassword().matches(".*[A-Z].*")) {
            throw new ValidationException("Password must contain at least one uppercase letter.");
        }
        if (!user.getPassword().matches(".*[!@#$%&*].*")) {
            throw new ValidationException("Password must have at least one special character (e.g., @, #, $, %, etc.).");
        }
        if (bindingResult.hasErrors()) {
            throw new ValidationException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        } else {
            {

                /**
                 * Generating a random code and sending it to the user's email
                 */

                String code = verificationService.generateVerificationCode();

                User tempUser = User.builder()
                        .email(email)
                        .password(user.getPassword())
                        .role("ROLE_USER")
                        .enabled(false)
                        .build();

                tempUserService.saveTempUser(email, tempUser);
                verificationEmailService.sendVerificationEmail(email, code);
                verificationService.saveVerificationCode(email, code);

                return "Registration successful. Check your email - %s for the verification code.".formatted(email);
            }
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
