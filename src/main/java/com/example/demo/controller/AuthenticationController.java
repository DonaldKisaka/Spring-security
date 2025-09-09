package com.example.demo.controller;


import com.example.demo.Service.AuthenticationService;
import com.example.demo.config.JwtService;
import com.example.demo.dto.LoginUser;
import com.example.demo.dto.RegisterUser;
import com.example.demo.dto.VerifyUser;
import com.example.demo.model.User;
import com.example.demo.responses.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(
            @RequestBody RegisterUser registerUser
    ) {
        User registeredUser = authenticationService.SignUp(registerUser);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(
            @RequestBody LoginUser loginUser
    ) {
        User authenticatedUser = authenticationService.authenticate(loginUser);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(
            @RequestBody VerifyUser verifyUser
    ) {
        try {
            authenticationService.verifyUser(verifyUser);
            return ResponseEntity.ok("Account verified successfully ");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(
            @RequestParam String email
    ) {
        try {
            authenticationService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code resent");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
