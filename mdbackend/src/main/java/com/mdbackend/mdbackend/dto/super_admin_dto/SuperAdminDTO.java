package com.mdbackend.mdbackend.dto.super_admin_dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuperAdminDTO {
    private Integer passengerId;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String password;

    public SuperAdminDTO(String fullName, Integer passengerId, String phoneNumber, String email) {
        this.passengerId = passengerId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}
