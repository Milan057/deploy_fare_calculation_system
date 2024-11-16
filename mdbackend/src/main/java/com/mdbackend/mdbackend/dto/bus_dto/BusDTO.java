package com.mdbackend.mdbackend.dto.bus_dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusDTO {
    private String busNumber;
    private String userName;
    private String password;
    private Integer busAdminId;
    private String uniqueName;
}
