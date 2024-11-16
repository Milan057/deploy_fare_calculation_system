package com.mdbackend.mdbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EsewaTransactionResponse {
    private String productId;
    private String productName;
    private String CLIENT_ID;
    private String SECRET_KEY;
    private Double amount;

    public EsewaTransactionResponse(
            String productId,
            String productName,
            String cLIENT_ID,
            String sECRET_KEY,
            Double amount) {
        this.productId = productId;
        this.productName = productName;
        CLIENT_ID = cLIENT_ID;
        SECRET_KEY = sECRET_KEY;
        this.amount = amount;
    }

}
