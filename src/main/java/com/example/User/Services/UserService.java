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
            metroCard.setCardNumber(buyMetroCardPayload.getCardNumber());
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


    @KafkaListener(topics = "check-in-topic" , groupId = "group_id")
    public void checkKafka(String message)
    {
        try {
            CheckInDTO checkInDTO = objectMapper.readValue(message, CheckInDTO.class);
            // Process your DTO here
            log.info("Received CheckInDTO: {}", checkInDTO);
        } catch (JsonProcessingException e) {
            log.error("Error converting string to DTO", e);
        }
    }








}
