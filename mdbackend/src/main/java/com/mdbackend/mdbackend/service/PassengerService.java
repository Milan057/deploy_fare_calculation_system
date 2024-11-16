package com.mdbackend.mdbackend.service;



import com.mdbackend.mdbackend.dto.passenger_dto.PassengerDTO;
import com.mdbackend.mdbackend.dto.passenger_dto.PessangerDetailDTO;

public interface PassengerService {
    public void savePassenger(PassengerDTO passenger);

    public boolean existsByEmail(String email);

    public boolean existsByPhoneNumber(String phoneNumber);

    public PessangerDetailDTO getPassengerDetails(String phoneNumber);
    public Object[] fetchPassengerDetail(Integer passengerId);
}
