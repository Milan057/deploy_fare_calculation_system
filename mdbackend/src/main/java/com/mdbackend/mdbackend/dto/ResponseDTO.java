package com.mdbackend.mdbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDTO {
    private String message;
    private String responseType;

    public ResponseDTO(String message, String responseType) {
        this.responseType = responseType;
        this.message = message;
    }

}
