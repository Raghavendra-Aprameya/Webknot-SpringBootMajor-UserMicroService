package com.example.User.Repository;

import com.example.User.Entity.MetroCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetroCardRepository extends JpaRepository<MetroCardEntity,Long> {
}
