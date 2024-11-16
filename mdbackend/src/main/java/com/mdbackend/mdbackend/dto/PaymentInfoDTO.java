package com.mdbackend.mdbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentInfoDTO {
    private Double amountToPay;
    private Integer noOfPassengerWithOutCard, noOfCardHolders;
    private Double distanceTravelled;
    private Integer paymentInfoId;
    private Double avilableAMount;

    public PaymentInfoDTO(Double amountToPay, Integer noOfPassengerWithOutCard, Integer noOfCardHolders,
            Double distanceTravelled, Integer paymentInfoId, Double avilableAMount) {
        this.amountToPay = amountToPay;
        this.noOfPassengerWithOutCard = noOfPassengerWithOutCard;
        this.noOfCardHolders = noOfCardHolders;
        this.distanceTravelled = distanceTravelled;
        this.paymentInfoId = paymentInfoId;
        this.avilableAMount = avilableAMount;
    }

}
