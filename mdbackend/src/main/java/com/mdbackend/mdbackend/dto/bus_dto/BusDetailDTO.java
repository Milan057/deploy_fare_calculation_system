package com.mdbackend.mdbackend.dto.bus_dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusDetailDTO {
    private String userName;
    private Integer busId;
    private String busNumber;

    public BusDetailDTO(String userName, Integer busId, String busNumber) {
        this.userName = userName;
        this.busId = busId;
        this.busNumber = busNumber;
    }

}
