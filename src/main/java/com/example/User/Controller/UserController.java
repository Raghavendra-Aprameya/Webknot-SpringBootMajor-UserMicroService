package com.example.User.Controller;

import com.example.User.Dto.buyMetroCardDto;
import com.example.User.Entity.MetroCardEntity;
import com.example.User.Entity.TravelHistoryEntity;
import com.example.User.Entity.UserEntity;
import com.example.User.Services.DependentServices;
import com.example.User.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private DependentServices dependentServices;

    @PostMapping("/buy-metro-card")
    public ResponseEntity<MetroCardEntity> buyMetroCard(@RequestBody buyMetroCardDto buyMetroCardPayload) {
        return ResponseEntity.ok(userService.buyMetroCard(buyMetroCardPayload));
    }

    @GetMapping("/travel-history/{userId}")
    public ResponseEntity<List<TravelHistoryEntity>> getTravelHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getTravelHistory(userId));
    }

    @DeleteMapping("/cancel-metro-pass/{userId}")
    public ResponseEntity<String> cancelMetroPass(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.cancelMetroPass(userId));
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<Optional<UserEntity>> getUserProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }


    @GetMapping("/validate-card/{userId}")
    public ResponseEntity<Boolean> validateCard(@PathVariable Long userId) {
        boolean isValid = dependentServices.validateMetroCard(userId);
        return ResponseEntity.ok(isValid);
    }
}
