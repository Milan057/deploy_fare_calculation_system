package com.mdbackend.mdbackend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdbackend.mdbackend.repo.FareRateRepo;
import com.mdbackend.mdbackend.service.FareRateService;

@Service
public class FareRateServiceImpl implements FareRateService {

    @Autowired
    private FareRateRepo fareRateRepo;

    @Override
    public Double amount(Double distance) {
        Double amount = fareRateRepo.findFareRateByDistance(distance).getRate();
        return amount;
    }

}
