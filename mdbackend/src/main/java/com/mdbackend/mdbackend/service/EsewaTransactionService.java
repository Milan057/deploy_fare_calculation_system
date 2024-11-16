package com.mdbackend.mdbackend.service;

import com.mdbackend.mdbackend.dto.EsewaTransactionResponse;
import com.mdbackend.mdbackend.dto.RequestForPayment;

public interface EsewaTransactionService {
    public EsewaTransactionResponse savEsewaTransaction(Integer passengerId, Double amount);

    public Double esewaPay(Integer passengerId);

    public Double esewaPayment(RequestForPayment forPayment);

}
