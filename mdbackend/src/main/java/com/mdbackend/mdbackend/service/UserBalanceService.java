package com.mdbackend.mdbackend.service;

public interface UserBalanceService {
    public Double availableBalance(Integer passengerId);

    public void reduceAmount(Integer passengerId,Double payAmount);

}
