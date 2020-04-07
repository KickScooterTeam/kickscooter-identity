package com.softserve.identityservice.controller;

import com.softserve.identityservice.model.AppUser;
import com.softserve.identityservice.model.SignUpDto;
import com.softserve.identityservice.model.UserInfoResponse;
import com.softserve.identityservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<UUID> signUp(@RequestBody SignUpDto request){
        AppUser user = userService.signUp(request);
        return ResponseEntity.ok(user.getVerifyToken());
    }

    @GetMapping("/activate/{token}")
    public ResponseEntity<Void> verifyAccount(@PathVariable UUID token) throws ServletException {
        userService.activateAccount(token);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/admin/users/{userId}/block")
    public ResponseEntity<Long> blockUser(@PathVariable UUID userId){
        return ResponseEntity.ok(userService.blockUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserInfoResponse> userResponse(@PathVariable UUID id){
        return ResponseEntity.ok(userService.userInfo(id));
    }
}
