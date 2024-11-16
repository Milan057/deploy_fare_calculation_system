package com.mdbackend.mdbackend.dto.bus_dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllBusDetailDTO {
    private String userName;
    private Integer busId;
    private String busNumber;
    private boolean active;

    public AllBusDetailDTO(String userName, Integer busId, String busNumber, boolean active) {
        this.userName = userName;
        this.busId = busId;
        this.busNumber = busNumber;
        this.active = active;
    }

}
