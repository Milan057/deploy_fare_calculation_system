package com.mdbackend.mdbackend.dto.passenger_dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PassengerDTO {
    private Integer passengerId;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String password;

    public PassengerDTO(String fullName, Integer passengerId, String phoneNumber, String email) {
        this.passengerId = passengerId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}
