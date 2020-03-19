package com.softserve.identityservice.controller;

import com.softserve.identityservice.model.SignInDto;
import com.softserve.identityservice.model.SignUpDto;
import com.softserve.identityservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody SignUpDto request){
        userService.signUp(request);
        return ResponseEntity.status(204).build();
    }

    @PostMapping("/sign-in")
    public ResponseEntity<Void> signIn(@RequestBody SignInDto request){
        userService.signIn(request);
        return ResponseEntity.status(204).build();
    }
}
