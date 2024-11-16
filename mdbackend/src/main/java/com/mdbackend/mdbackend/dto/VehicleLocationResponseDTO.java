package com.mdbackend.mdbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleLocationResponseDTO {
    private String lat;
    private String lon;

    public VehicleLocationResponseDTO(String lat, String lon) {
        this.lat = lat;
        this.lon = lon;
    }

}
