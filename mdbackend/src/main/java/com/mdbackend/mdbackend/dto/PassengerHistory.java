package com.mdbackend.mdbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PassengerHistory {
    private String ticketCreatedDate;
    private Integer paymentStatus;
    private Double payAmount;
    private Integer noOfCardHolders, noOfPassenger;
    private String qrData;

}
