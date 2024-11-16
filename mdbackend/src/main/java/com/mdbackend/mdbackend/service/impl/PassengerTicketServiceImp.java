package com.mdbackend.mdbackend.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdbackend.mdbackend.dto.PassengerHistory;
import com.mdbackend.mdbackend.dto.PassengerTicketDTO;
import com.mdbackend.mdbackend.dto.PassengerTicketResponse;
import com.mdbackend.mdbackend.entities.PassengerTicket;
import com.mdbackend.mdbackend.exceptions.ClientException;
import com.mdbackend.mdbackend.repo.PassengerTicketRepo;
import com.mdbackend.mdbackend.service.PassengerTicketService;
import com.mdbackend.mdbackend.utilis.DateConvertor;

@Service
public class PassengerTicketServiceImp implements PassengerTicketService {

    @Autowired
    private PassengerTicketRepo passengerTicketRepo;
    @Autowired
    private DateConvertor convertor;
    @Autowired
    private TicketQRServiceImp imp;

    @Override
    public PassengerTicketResponse savePassengerTicket(PassengerTicketDTO dto) throws Exception {
        PassengerTicket passengerTicket = new PassengerTicket();
        passengerTicket.setCreatedDate(new Date());
        passengerTicket.setDelFlg(false);
        passengerTicket.setPassengerId(dto.getCustomerId());
        Date dateTime = convertor.convertToDate(dto.getScannedTime());
        passengerTicket.setScannedTime(dateTime);
        Integer qrId = imp.findQrIdFromData(dto.getQrData());
        passengerTicket.setTicketQrId(qrId);
        if(qrId==null){
            throw new ClientException("Ticket id not found");
        }
        passengerTicketRepo.save(passengerTicket);
        List<Object[]> listOfResponse = passengerTicketRepo.returnTicketResponse(passengerTicket.getId());
        if (!listOfResponse.isEmpty()) {
            Object[] row = listOfResponse.get(0);
            String busNumber = (String) row[0];
            Integer busId = ((Number) row[1]).intValue();
            Date recordedTime = (Date) row[2];
            String recordedDate = convertor.convertToString(recordedTime);
            Integer numberOfPassengers = ((Number) row[3]).intValue();
            Integer numberOfCardholders = ((Number) row[4]).intValue();
            Integer tripId = ((Number) row[5]).intValue();
            return new PassengerTicketResponse(busNumber, recordedDate, numberOfPassengers, numberOfCardholders, busId,
                    tripId);
        }
        return null;

    }

    public boolean existsByQRId(String qrData) {
        Integer qrId = imp.findQrIdFromData(qrData);
        return passengerTicketRepo.existsByTicketQrId(qrId);
    }

    @Override
    public List<PassengerHistory> fetchHistory(Integer passengerId) {

        List<Object[]> listOfHistory = passengerTicketRepo.returnPassnegerHistory(passengerId);
        List<PassengerHistory> histories = new ArrayList<>();
        listOfHistory.forEach(list -> {
            PassengerHistory history = new PassengerHistory();
            history.setTicketCreatedDate(list[0].toString());
            history.setPaymentStatus(((Number) list[1]).intValue());
            history.setPayAmount(((Number) list[2]).doubleValue());
            history.setNoOfCardHolders(((Number) list[3]).intValue());
            history.setNoOfPassenger(((Number) list[4]).intValue());
            history.setQrData(list[5].toString());
            histories.add(history);

        });
        return histories;
    }

    @Override
    public Object[] fetchTicketStatus(Integer passeengerId) {
        return passengerTicketRepo.fetchTicketStatus(passeengerId);
    }

    @Override
    public PassengerTicketResponse fetchTicketDetail(String qrData) {
        Integer qrId = imp.findQrIdFromData(qrData);
        List<Object[]> listOfResponse = passengerTicketRepo.returnPaymentResponse(qrId);
        if (!listOfResponse.isEmpty()) {
            Object[] row = listOfResponse.get(0);
            String busNumber = (String) row[0];
            Integer busId = ((Number) row[1]).intValue();
            Date recordedTime = (Date) row[2];
            String recordedDate = convertor.convertToString(recordedTime);
            Integer numberOfPassengers = ((Number) row[3]).intValue();
            Integer numberOfCardholders = ((Number) row[4]).intValue();
            Integer tripId = ((Number) row[5]).intValue();
            return new PassengerTicketResponse(busNumber, recordedDate, numberOfPassengers, numberOfCardholders, busId,
                    tripId);
        } else {
            return null;
        }
    }

}
