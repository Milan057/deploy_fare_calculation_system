package com.mdbackend.mdbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketQRResponse {
    private String recordedTimestamp;
    private Integer numberOfPassengers, numberOfCardHolders;
    private String qrData;

    public TicketQRResponse(String recordedTimestamp, Integer numberOfPassengers, Integer numberOfCardHolders,
            String qrData) {
        this.recordedTimestamp = recordedTimestamp;
        this.numberOfPassengers = numberOfPassengers;
        this.numberOfCardHolders = numberOfCardHolders;
        this.qrData = qrData;
    }

}
