package com.example.User.Repository;

import com.example.User.Entity.TravelHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TravelHistoryRepository extends JpaRepository<TravelHistoryEntity,Long> {
    List<TravelHistoryEntity> findTop10ByUserIdOrderByTravelTimeDesc(Long userId);
}
