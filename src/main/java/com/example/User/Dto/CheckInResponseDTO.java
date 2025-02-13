package com.example.User.Dto;

import lombok.Data;

@Data
public class CheckInResponseDTO {
    private Long userId;
    private String stationName;
    private String status;  // "Success" or "Failed"
    private String message;
}
