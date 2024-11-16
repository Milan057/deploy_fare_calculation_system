package com.mdbackend.mdbackend.service;

import java.util.List;
import java.util.Map;

import com.mdbackend.mdbackend.dto.HistoryForBus;
import com.mdbackend.mdbackend.dto.TicketQRDTO;
import com.mdbackend.mdbackend.dto.TicketQRResponse;
import com.mdbackend.mdbackend.entities.TicketQR;

public interface TicketQRService {
    public TicketQRResponse saveTicketQR(TicketQRDTO dto);

    public Integer findQrIdFromData(String qrDate);

    public TicketQR noOfPassenger(String qrData);

    public Object[] fetchTicketStatus(Integer tripId);

    public Integer fetchTripId(String qrData);

    public Map<Integer, List<HistoryForBus>> fetchHistoryForBus(Integer busId);
}
