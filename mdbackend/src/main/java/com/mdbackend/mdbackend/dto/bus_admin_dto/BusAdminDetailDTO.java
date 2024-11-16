package com.mdbackend.mdbackend.dto.bus_admin_dto;

import lombok.*;

@Getter
@Setter
public class BusAdminDetailDTO {
    private Integer busAdminId;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String shortName;

    public BusAdminDetailDTO(Integer busAdminId, String fullName, String phoneNumber, String email,String shortName) {
        this.busAdminId = busAdminId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.shortName=shortName;
    }
}
