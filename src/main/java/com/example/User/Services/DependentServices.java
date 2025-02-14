//package com.example.User.Services;
//
//import com.example.User.Entity.MetroCardEntity;
//import com.example.User.Entity.UserEntity;
//import com.example.User.Repository.MetroCardRepository;
//import com.example.User.Repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//import java.util.Optional;
//
//@Service
//public class DependentServices {
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    MetroCardRepository metroCardRepository;
//
//    public boolean validateMetroCard(Long userId)
//    {
//        Optional<UserEntity> user= userRepository.findById(userId);
//        Long metroCardId = user.get().getMetroCard().getId();
//        double balance= metroCardRepository.findById(metroCardId).get().getBalance();
//        if(balance>=50)
//            return true;
//        return false;
//
//    }
//}
package com.example.User.Services;

import com.example.User.Entity.MetroCardEntity;
import com.example.User.Entity.UserEntity;
import com.example.User.Repository.MetroCardRepository;
import com.example.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DependentServices {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MetroCardRepository metroCardRepository;

    public boolean validateMetroCard(Long userId) {
        Optional<UserEntity> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return false; // User not found
        }

        UserEntity user = userOpt.get();
        MetroCardEntity metroCard = user.getMetroCard();
        if (metroCard == null) {
            return false; // No metro card linked
        }


        return metroCard.getBalance() >= 50; // Ensure sufficient balance
    }
}
