package com.mdbackend.mdbackend.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdbackend.mdbackend.entities.UserBalance;
import com.mdbackend.mdbackend.repo.UserBalanceRepo;
import com.mdbackend.mdbackend.service.UserBalanceService;

@Service
public class UserBalanceServiceImpl implements UserBalanceService {

    @Autowired
    private UserBalanceRepo balanceRepo;

    @Override
    public Double availableBalance(Integer passengerId) {
        UserBalance balance = balanceRepo.returnAvailableAmount(passengerId);
        return balance.getAvailabeBalance();
    }

    @Override
    public void reduceAmount(Integer passengerId, Double payAmount) {
        UserBalance balance = balanceRepo.returnAvailableAmount(passengerId);
        Double availabeBalance = balance.getAvailabeBalance() - payAmount;
        balance.setAvailabeBalance(availabeBalance);
        balance.setModifiedDate(new Date());
        balanceRepo.save(balance);
    }

}
