package com.mdbackend.mdbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaymentRequest {
    private String qrData;
    private Integer passengerId;
    private Integer busId;
    private Integer tripId;
}