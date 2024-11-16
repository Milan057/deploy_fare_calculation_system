package com.mdbackend.mdbackend.service;

import java.util.List;

import com.mdbackend.mdbackend.dto.PassengerHistory;
import com.mdbackend.mdbackend.dto.PassengerTicketDTO;
import com.mdbackend.mdbackend.dto.PassengerTicketResponse;

public interface PassengerTicketService {
    public PassengerTicketResponse savePassengerTicket(PassengerTicketDTO dto) throws Exception;

    public boolean existsByQRId(String qrId);

    public List<PassengerHistory> fetchHistory(Integer passengerId);

    public Object[] fetchTicketStatus(Integer passeengerId);

    public PassengerTicketResponse fetchTicketDetail(String qrData);
}
