package com.mdbackend.mdbackend.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketQRDTO {
    private String requestedTimestamp;
    private Integer numberOfPassengers,numberOfCardHolders,busId,tripId;
}
