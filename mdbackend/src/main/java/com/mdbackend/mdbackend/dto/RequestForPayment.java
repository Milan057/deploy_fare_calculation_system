package com.mdbackend.mdbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class RequestForPayment {
    private Integer passengerId;
    private String productId;
    private String refId;

}
