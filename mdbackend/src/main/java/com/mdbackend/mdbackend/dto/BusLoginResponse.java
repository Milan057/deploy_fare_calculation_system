package com.mdbackend.mdbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusLoginResponse {
    private String token;
    private Integer id;
    private Integer tripId;

    public BusLoginResponse(String token, Integer id, Integer tripId ) {
        this.token = token;
        this.id = id;
        this.tripId = tripId;
    }
}
