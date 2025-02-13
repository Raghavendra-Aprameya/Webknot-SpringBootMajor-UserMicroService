package com.example.User.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private MetroCardEntity metroCard;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private List<TravelHistoryEntity> travelHistory;
}
