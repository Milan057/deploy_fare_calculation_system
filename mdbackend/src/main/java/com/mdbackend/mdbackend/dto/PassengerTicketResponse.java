package com.mdbackend.mdbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PassengerTicketResponse {
  private String busNumber;
  private String ticketCreatedTime;
  private Integer numberOfPassengers;
  private Integer numberOfCardHolders;
  private Integer busId;
  private Integer tripId;

  public PassengerTicketResponse(String busNumber, String ticketCreatedTime, Integer numberOfPassengers,
      Integer numberOfCardHolders, Integer busId, Integer tripId) {
    this.busNumber = busNumber;
    this.ticketCreatedTime = ticketCreatedTime;
    this.numberOfPassengers = numberOfPassengers;
    this.numberOfCardHolders = numberOfCardHolders;
    this.busId = busId;
    this.tripId = tripId;
  }

}
