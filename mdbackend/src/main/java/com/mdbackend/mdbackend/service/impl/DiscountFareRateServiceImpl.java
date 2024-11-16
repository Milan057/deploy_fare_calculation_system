package com.mdbackend.mdbackend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdbackend.mdbackend.repo.DiscountFareRateRepo;
import com.mdbackend.mdbackend.service.DiscountFareRateService;

@Service
public class DiscountFareRateServiceImpl implements DiscountFareRateService {

    @Autowired
    private DiscountFareRateRepo fareRateRepo;

    @Override
    public Double amount(Double distance) {
        Double amount = fareRateRepo.findFareRateByDistance(distance).getRate();
        return amount;
    }

}
