package com.mdbackend.mdbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EsewaTransactionRequest {
    private Integer passengerId;
    private Double amount;
}
