package com.example.User.Dto;

import lombok.Data;

@Data
public class buyMetroCardDto {
    private Long userId;
    private String cardNumber;
    private Double initialBalance;
}
