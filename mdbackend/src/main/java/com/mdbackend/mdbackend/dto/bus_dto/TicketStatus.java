package com.mdbackend.mdbackend.dto.bus_dto;

import java.util.List;

import com.mdbackend.mdbackend.dto.VehicleLocationResponseDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketStatus {
    private Integer totalTicket;
    private Integer paymentComplete;
    private Integer paymentPending;
    private List<VehicleLocationResponseDTO> vehicleLocations;

    public TicketStatus(Integer total, Integer active, Integer inactive, List<VehicleLocationResponseDTO> dtos) {
        this.totalTicket = total;
        this.paymentComplete = active;
        this.paymentPending = inactive;
        this.vehicleLocations = dtos;
    }

}
