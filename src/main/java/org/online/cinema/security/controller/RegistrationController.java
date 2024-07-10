package org.online.cinema.security.controller;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.online.cinema.common.dto.UserDTO;
import org.online.cinema.common.exception.UserException;
import org.online.cinema.security.service.TempUserService;
import org.online.cinema.security.service.registration.VerificationEmailService;
import org.online.cinema.security.service.registration.VerificationService;
import org.online.cinema.user.entity.User;
import org.online.cinema.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@Slf4j
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

    @Value("${default.user.role}")
    private String defaultRole;

    @PostMapping("/registration")
    public String registerUser(@Valid @RequestBody UserDTO user, BindingResult bindingResult) throws MessagingException {

        String email = user.getEmail();

        log.info("User registration: email={}", email);

        /**
         * Validation adding
         */

        if (userService.findByEmail(email) != null) {
            log.error("User already exist: email={}", email);
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
            log.error("User registration validation failed: email={}, status={}", email, HttpStatus.BAD_REQUEST.getReasonPhrase());
            throw new ValidationException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        } else {
            {

                /**
                 * Generating a random code and sending it to the user's email
                 */

                log.info("Registration successful: email={}, status={}", email, HttpStatus.OK.getReasonPhrase());

                String code = verificationService.generateVerificationCode();

                log.info("Generating a verification code: email={}", email);

                User tempUser = User.builder()
                        .email(email)
                        .password(user.getPassword())
                        .role(defaultRole)
                        .enabled(false)
                        .build();

                tempUserService.saveTempUser(email, tempUser);
                verificationService.saveVerificationCode(email, code);
                verificationEmailService.sendVerificationEmail(email, code);

                return "Registration successful. Check your email - %s for the verification code.".formatted(email);
            }
        }
    }


    @SneakyThrows
    @PostMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestBody Map<String, String> request) {
        if (!request.containsKey("email") || !request.containsKey("code")) {
            log.error("Email and code must be provided: status={}", HttpStatus.BAD_REQUEST.getReasonPhrase());
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
                verificationService.deleteVerificationCode(email);
                log.info("Email verified successfully: email={}, status={}", email, HttpStatus.OK.getReasonPhrase());
                return ResponseEntity.ok("Email verified successfully.");
            } else {
                log.error("No user data found for the provided email: email={}, status={}", email, HttpStatus.BAD_REQUEST.getReasonPhrase());
                return ResponseEntity.badRequest().body("No user data found for the provided email.");
            }
        } else {
            log.error("Invalid or expired verification code: email={}, status={}", email, HttpStatus.BAD_REQUEST.getReasonPhrase());
            return ResponseEntity.badRequest().body("Invalid or expired verification code.");
        }
    }
}
