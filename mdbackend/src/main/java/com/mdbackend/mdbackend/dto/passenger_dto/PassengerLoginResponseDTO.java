package com.mdbackend.mdbackend.dto.passenger_dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PassengerLoginResponseDTO {
    private Integer passengerId;
    private String fullName;
    private String phoneNumber;
    private String email;
    private Double availableBalance;
    private String token;

    public PassengerLoginResponseDTO(Integer passengerId, String fullName, String phoneNumber, String email,
            Double availableBalance, String token) {
        this.passengerId = passengerId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.availableBalance = availableBalance;
        this.token = token;
    }

}
