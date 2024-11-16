package com.mdbackend.mdbackend.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdbackend.mdbackend.dto.HistoryForBus;
import com.mdbackend.mdbackend.dto.TicketQRDTO;
import com.mdbackend.mdbackend.dto.TicketQRResponse;
import com.mdbackend.mdbackend.entities.TicketQR;
import com.mdbackend.mdbackend.entities.VehicleTrip;
import com.mdbackend.mdbackend.repo.VehicleQRRepo;
import com.mdbackend.mdbackend.repo.VehicleTripRepo;
import com.mdbackend.mdbackend.service.TicketQRService;
import com.mdbackend.mdbackend.utilis.DateConvertor;
import com.mdbackend.mdbackend.utilis.Generator;

@Service
public class TicketQRServiceImp implements TicketQRService {
    @Autowired
    private Generator generatedKeys;
    @Autowired
    private VehicleQRRepo qrRepo;
    @Autowired
    private DateConvertor convertor;
    @Autowired
    private VehicleTripRepo vehicleTripRepo;

    public TicketQRResponse saveTicketQR(TicketQRDTO dto) {
        TicketQR qr = new TicketQR();
        qr.setBusId(dto.getBusId());
        qr.setCreatedDate(new Date());
        qr.setDelFlg(false);
        qr.setNumberOfCardHolders(dto.getNumberOfCardHolders());
        qr.setNumberOfPassengers(dto.getNumberOfPassengers());
        String word = generatedKeys.generateUniqueRandomKey();
        Optional<VehicleTrip> vehicleTrip = vehicleTripRepo.findById(dto.getTripId());
        if (vehicleTrip.isPresent()) {
            qr.setTripId(vehicleTrip.get());
        }
        qr.setSecretKey(word);
        Date dateTime = convertor.convertToDate(dto.getRequestedTimestamp());
        qr.setRecordedTime(dateTime);
        qrRepo.save(qr);
        String recordedTime = convertor.convertToString(qr.getRecordedTime());
        return new TicketQRResponse(recordedTime, qr.getNumberOfPassengers(), qr.getNumberOfCardHolders(),
                qr.getSecretKey());
    }

    @Override
    public Integer findQrIdFromData(String qrDate) {
        return qrRepo.findByQrData(qrDate);
    }

    @Override
    public TicketQR noOfPassenger(String qrData) {
        return qrRepo.returnNumberofPasseneger(qrData);
    }

    @Override
    public Object[] fetchTicketStatus(Integer tripId) {
        return qrRepo.fetchTicketStatus(tripId);
    }

    @Override
    public Integer fetchTripId(String qrData) {
        return qrRepo.fetchTripId(qrData);
    }

 @Override
public Map<Integer, List<HistoryForBus>> fetchHistoryForBus(Integer busId) {
    List<Object[]> list = qrRepo.fetchHistoryForBus(busId);
    Map<Integer, List<HistoryForBus>> historyMap = new LinkedHashMap<>();

    list.forEach(record -> {
        Integer tripId = (Integer) record[0];
        Integer noOfCard = (Integer) record[1]; 
        Integer noOfPassenger = (Integer) record[2]; 
        String scannedTime = (String) record[3];
        String passengerName = (String) record[4]; 
        Double amount = Double.valueOf((String) record[5]); 
        Double distance = Double.valueOf((String) record[6]); 
        String ticketStatus = (String) record[7]; 
        String paymentStatusDescription = (String) record[8];
        String qrData=(String)record[9];
        String createdTime=new DateConvertor().shortDateTime.format((Date)record[10]);



        HistoryForBus history = new HistoryForBus( noOfCard, noOfPassenger, scannedTime, passengerName, amount, distance, ticketStatus, paymentStatusDescription,
        qrData,createdTime);
        historyMap.computeIfAbsent(tripId, k -> new ArrayList<>()).add(history);
    });

    return historyMap;
}

}
