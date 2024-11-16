package com.mdbackend.mdbackend.dto.bus_dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PassengerTicketStatus {
    private Integer totalTicket;
    private Integer paymentComplete;
    private Integer paymentPending;
    private Double amount;

    public PassengerTicketStatus(Integer total, Integer active, Integer inactive, Double amount) {
        this.totalTicket = total;
        this.paymentComplete = active;
        this.paymentPending = inactive;
        this.amount = amount;
    }

}
