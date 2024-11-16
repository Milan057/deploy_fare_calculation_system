package com.mdbackend.mdbackend.service;

import com.mdbackend.mdbackend.entities.PaymentInfo;

public interface PaymentInfoService {
    public Integer savePaymentInfo(Double totalAmountToPay, Integer tripId, Integer busId, Integer passengerId,
            Integer noOfCard, Integer passengerWithoutCard, String qrData,Double totalDistance);

    public PaymentInfo returnPaymentInfoById(Integer paymentId);

    public Object[] fetchPaymentInfo(Integer qrId, Integer tripId);

}
