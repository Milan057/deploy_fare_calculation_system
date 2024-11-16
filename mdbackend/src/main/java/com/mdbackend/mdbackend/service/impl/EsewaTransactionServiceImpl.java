package com.mdbackend.mdbackend.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdbackend.mdbackend.dto.EsewaTransactionResponse;
import com.mdbackend.mdbackend.dto.RequestForPayment;
import com.mdbackend.mdbackend.entities.EsewaTransaction;
import com.mdbackend.mdbackend.entities.Passenger;
import com.mdbackend.mdbackend.entities.UserBalance;
import com.mdbackend.mdbackend.repo.EsewaTransactionRepo;
import com.mdbackend.mdbackend.repo.PassengerRepo;
import com.mdbackend.mdbackend.repo.UserBalanceRepo;
import com.mdbackend.mdbackend.service.EsewaTransactionService;
import com.mdbackend.mdbackend.utilis.DateConvertor;

@Service
public class EsewaTransactionServiceImpl implements EsewaTransactionService {
    @Autowired
    private PassengerRepo passengerRepo;
    @Autowired
    private EsewaTransactionRepo esewaTransactionRepo;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserBalanceRepo balanceRepo;

    @Override
    public EsewaTransactionResponse savEsewaTransaction(Integer passengerId, Double amount) {
        String CLIENT_ID = "JB0BBQ4aD0UqIThFJwAKBgAXEUkEGQUBBAwdOgABHD4DChwUAB0R",
                SECRET_KEY = "BhwIWQQADhIYSxILExMcAgFXFhcOBwAKBgAXEQ==";
        EsewaTransaction esewaTransaction = new EsewaTransaction();
        esewaTransaction.setDelFlg(false);
        Optional<Passenger> passenger = passengerRepo.findById(passengerId);
        if (passenger.isPresent()) {
            esewaTransaction.setPassengerId(passenger.get());
        }
        esewaTransaction.setAmount(amount);
        Random random = new Random();
        int randomInt = Math.abs(random.nextInt());
        DateConvertor convertor = new DateConvertor();
        Long dateInSecond = convertor.returnSecond(new Date());
        esewaTransaction.setProductId((randomInt + "_" + dateInSecond));
        esewaTransaction.setStatus("");
        esewaTransaction.setProductName("Esewa Topup");
        esewaTransaction.setRefId("");
        esewaTransaction.setCreatedDate(new Date());
        esewaTransactionRepo.save(esewaTransaction);
        EsewaTransactionResponse esewaTransactionResponse = new EsewaTransactionResponse(
                esewaTransaction.getProductId(),
                esewaTransaction.getProductName(), CLIENT_ID, SECRET_KEY, esewaTransaction.getAmount());
        return esewaTransactionResponse;

    }

    public Double esewaPay(Integer passengerId) {
        List<Object[]> list = esewaTransactionRepo.fetchPendingEsewaTransaction(passengerId);
        ObjectMapper objectMapper = new ObjectMapper();
        Double totalCalculatedAmount = 0.0;

        if (list != null && !list.isEmpty()) {
            for (Object[] transaction : list) {
                Integer id = (Integer) transaction[0];
                String productCode = (String) transaction[2];
                Double totalAmount = (Double) transaction[1];

                String url = String.format(
                        "https://uat.esewa.com.np/api/epay/transaction/status/?product_code=EPAYTEST&total_amount=%.2f&transaction_uuid=%s",
                        totalAmount, productCode);

                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

                if (response.getStatusCode().is2xxSuccessful()) {
                    String responseBody = response.getBody();
                    System.out.println("eSewa API Response: " + responseBody);
                    JsonNode jsonNode;

                    try {
                        jsonNode = objectMapper.readTree(response.getBody());
                        String refId = jsonNode.path("refId").asText();
                        String status = jsonNode.path("status").asText();

                        EsewaTransaction esewaTransaction = esewaTransactionRepo.getById(id);
                        esewaTransaction.setRefId(refId);
                        esewaTransaction.setStatus(status);

                        if ("COMPLETE".equalsIgnoreCase(status)) {
                            UserBalance balance = balanceRepo.returnAvailableAmount(passengerId);
                            if (balance != null) {
                                balance.setAvailabeBalance(balance.getAvailabeBalance() + totalAmount);
                                balance.setModifiedDate(new Date());
                                balanceRepo.save(balance);
                                totalCalculatedAmount = balance.getAvailabeBalance();
                            } else {
                                UserBalance userBalance = new UserBalance();
                                userBalance.setAvailabeBalance(totalAmount);
                                userBalance.setCreatedDate(new Date());
                                userBalance.setPassengerId(passengerId);
                                userBalance.setModifiedDate(new Date());
                                userBalance.setDelFlg(false);
                                balanceRepo.save(userBalance);
                                totalCalculatedAmount = userBalance.getAvailabeBalance();

                            }
                        }
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                } else {
                    System.out.println("Error calling eSewa API: " + response.getStatusCode());
                }
            }
        }

        return totalCalculatedAmount;
    }

    @Override
    public Double esewaPayment(RequestForPayment forPayment) {
        Double totalAmount = 0.0;
        EsewaTransaction esewaTransaction = esewaTransactionRepo.fetchEsewaTransaction(forPayment.getPassengerId(),
                forPayment.getProductId());
        if (esewaTransaction != null) {
            Double amount = esewaTransaction.getAmount();
            esewaTransaction.setRefId(forPayment.getRefId());
            esewaTransaction.setStatus("Completed");
            esewaTransactionRepo.save(esewaTransaction);
            UserBalance balance = balanceRepo.returnAvailableAmount(forPayment.getPassengerId());
            if (balance != null) {
                balance.setAvailabeBalance(balance.getAvailabeBalance() + amount);
                balance.setModifiedDate(new Date());
                balanceRepo.save(balance);
                totalAmount = balance.getAvailabeBalance();
            } else {
                UserBalance userBalance = new UserBalance();
                userBalance.setAvailabeBalance(amount);
                userBalance.setCreatedDate(new Date());
                userBalance.setPassengerId(forPayment.getPassengerId());
                userBalance.setModifiedDate(new Date());
                userBalance.setDelFlg(false);
                balanceRepo.save(userBalance);
                totalAmount = userBalance.getAvailabeBalance();
            }
        }
        return totalAmount;

    }

}
