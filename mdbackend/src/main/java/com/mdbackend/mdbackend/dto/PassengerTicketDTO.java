package com.mdbackend.mdbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PassengerTicketDTO {
private String qrData;
private Integer customerId;
private String scannedTime;
}
