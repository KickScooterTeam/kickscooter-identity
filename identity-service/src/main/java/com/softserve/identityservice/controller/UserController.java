package com.softserve.identityservice.controller;

import com.softserve.identityservice.model.AppUser;
import com.softserve.identityservice.model.SignInDto;
import com.softserve.identityservice.model.SignUpDto;
import com.softserve.identityservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<UUID> signUp(@RequestBody SignUpDto request){
        AppUser user = userService.signUp(request);
        return ResponseEntity.ok(user.getVerifyToken());
    }

    @PostMapping("/sign-in")
    public ResponseEntity<Void> signIn(@RequestBody SignInDto request){
        userService.signIn(request);
        return ResponseEntity.status(204).build();
    }

    @GetMapping("/activate/{token}")
    public ResponseEntity<Void> verifyAccount(@PathVariable UUID token) throws ServletException {
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", userService.activateAccount(token));
        return new ResponseEntity(header, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/admin/block/{id}")
    public ResponseEntity<Long> blockUser(@PathVariable UUID id){
        return ResponseEntity.ok(userService.blockUser(id));
    }
}
