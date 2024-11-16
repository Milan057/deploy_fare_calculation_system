package com.mdbackend.mdbackend.dto.passenger_dto;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class PessangerDetailDTO {
    private Integer passengerId;
    private String fullName;
    private String phoneNumber;
    private String email;
    public PessangerDetailDTO(Integer passengerId,String fullName,String phoneNumber,String email){
        this.email=email;
        this.fullName=fullName;
        this.phoneNumber=phoneNumber;
        this.passengerId=passengerId;
    }
}
