package com.example.User.Services;

import com.example.User.Dto.CheckInDTO;
import com.example.User.Dto.buyMetroCardDto;
import com.example.User.Entity.MetroCardEntity;
import com.example.User.Entity.TravelHistoryEntity;
import com.example.User.Entity.UserEntity;
import com.example.User.Repository.MetroCardRepository;
import com.example.User.Repository.TravelHistoryRepository;
import com.example.User.Repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MetroCardRepository metroCardRepository;

    @Autowired
    private TravelHistoryRepository travelHistoryRepository;
    @Autowired
    private KafkaTemplate kafkaTemplate;

    private final ObjectMapper objectMapper=new ObjectMapper();

    // Buy Metro Card
    public MetroCardEntity buyMetroCard(buyMetroCardDto buyMetroCardPayload) {
        Optional<UserEntity> userOptional = userRepository.findById(buyMetroCardPayload.getUserId());
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            MetroCardEntity metroCard = new MetroCardEntity();
            metroCard.setCardNumber(UUID.randomUUID().toString());
            metroCard.setBalance(buyMetroCardPayload.getInitialBalance());
            metroCard.setUser(user);
            return metroCardRepository.save(metroCard);
        }
        throw new RuntimeException("User not found");
    }

    // Retrieve Travel History (Last 10 Trips)
    public List<TravelHistoryEntity> getTravelHistory(Long userId) {
        return travelHistoryRepository.findTop10ByUserIdOrderByTravelTimeDesc(userId);
    }

    // Cancel Metro Pass
    public String cancelMetroPass(Long userId) {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            if (user.getMetroCard() != null) {
                metroCardRepository.delete(user.getMetroCard());
                return "Metro pass canceled successfully.";
            } else {
                return "User does not have an active metro pass.";
            }
        }
        return "User not found.";
    }

    // Fetch User Profile
    public Optional<UserEntity> getUserProfile(Long userId) {
        return userRepository.findById(userId);
    }

//
//    @KafkaListener(topics = "check-in-topic" , groupId = "group_id")
//    public void checkKafka(String message)
//    {
//        try {
//            CheckInDTO checkInDTO = objectMapper.readValue(message, CheckInDTO.class);
//            // Process your DTO here
//
//            log.info("Received CheckInDTO: {}", checkInDTO);
//        } catch (JsonProcessingException e) {
//            log.error("Error converting string to DTO", e);
//        }
//    }
@KafkaListener(topics = "check-in-topic", groupId = "group_id")
public void checkKafka(String message) {
    try {
        CheckInDTO checkInDTO = objectMapper.readValue(message, CheckInDTO.class);
        Long userId = checkInDTO.getUserId();

        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            MetroCardEntity metroCard = userOptional.get().getMetroCard();
            if (metroCard != null && metroCard.getBalance() >= 100) {
                kafkaTemplate.send("check-in-response-topic", "true");
                log.info("Check-in valid: User {} has sufficient balance.", userId);
            } else {
                kafkaTemplate.send("check-in-response-topic", "false");
                log.warn("Check-in denied: User {} has insufficient balance.", userId);
            }
        } else {
            kafkaTemplate.send("check-in-response-topic", "false");
            log.warn("Check-in denied: User {} not found.", userId);
        }
    } catch (JsonProcessingException e) {
        log.error("Error parsing check-in message", e);
    }
}








}
