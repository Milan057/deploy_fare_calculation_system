package com.mdbackend.mdbackend.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoryForBus {
    private Integer noOfCard;
    private Integer noOfPassenger;
    private String scannedTime;
    private String passengerName;
    private Double amount;
    private Double distance;
    private String ticketStatus;
    private String paymentStatusDescription;
    private String qrData;
    private String createdTime;


    public HistoryForBus(Integer noOfCard, Integer noOfPassenger, String scannedTime, String passengerName,
            Double amount, Double distance, String ticketStatus, String paymentStatusDescription,
            String qrData,String createdTime
            ) {
        this.noOfCard = noOfCard;
        this.noOfPassenger = noOfPassenger;
        this.scannedTime = scannedTime;
        this.passengerName = passengerName;
        this.amount = amount;
        this.distance = distance;
        this.ticketStatus = ticketStatus;
        this.paymentStatusDescription = paymentStatusDescription;
        this.qrData=qrData;
        this.createdTime=createdTime;
    }

}
