package com.mdbackend.mdbackend.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdbackend.mdbackend.entities.Bus;
import com.mdbackend.mdbackend.entities.Passenger;
import com.mdbackend.mdbackend.entities.PaymentInfo;
import com.mdbackend.mdbackend.entities.TicketQR;
import com.mdbackend.mdbackend.entities.VehicleTrip;
import com.mdbackend.mdbackend.repo.BusRepo;
import com.mdbackend.mdbackend.repo.PassengerRepo;
import com.mdbackend.mdbackend.repo.PaymentInfoRepo;
import com.mdbackend.mdbackend.repo.VehicleQRRepo;
import com.mdbackend.mdbackend.repo.VehicleTripRepo;
import com.mdbackend.mdbackend.service.PaymentInfoService;

@Service
public class PaymentInfoServiceImpl implements PaymentInfoService {

    @Autowired
    private PaymentInfoRepo infoRepo;
    @Autowired
    private VehicleTripRepo vehicleTrip;
    @Autowired
    private BusRepo busRepo;
    @Autowired
    private PassengerRepo passengerRepo;
    @Autowired
    private VehicleQRRepo qrRepo;

    @Override
    public Integer savePaymentInfo(Double totalAmountToPay, Integer tripId, Integer busId, Integer passengerId,
            Integer noOfCard, Integer passengerWithoutCard, String qrData,Double totalDistance) {
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setPayingAmount(totalAmountToPay);
        Optional<Bus> bus = busRepo.findById(busId);
        if (bus.isPresent()) {
            paymentInfo.setBusId(bus.get());
        }
        Optional<Passenger> passenger = passengerRepo.findById(passengerId);
        if (passenger.isPresent()) {
            paymentInfo.setPassengerId(passenger.get());
        }
        Optional<VehicleTrip> trip = vehicleTrip.findById(tripId);
        if (trip.isPresent()) {
            paymentInfo.setTripId(trip.get());
        }
        paymentInfo.setNoOfCardHolder(noOfCard);
        paymentInfo.setPassengerWithoutCard(passengerWithoutCard);
        Integer qrId = qrRepo.findByQrData(qrData);
        Optional<TicketQR> qr = qrRepo.findById(qrId);
        if (qr.isPresent()) {
            paymentInfo.setQrId(qr.get());
        }
        paymentInfo.setDistanceTravel(totalDistance);
        infoRepo.save(paymentInfo);
        return paymentInfo.getId();

    }

    @Override
    public PaymentInfo returnPaymentInfoById(Integer paymentId) {
        Optional<PaymentInfo> paymentInfoDetail = infoRepo.findById(paymentId);
        paymentInfoDetail.get().setPaymentStatus(1);
        infoRepo.save(paymentInfoDetail.get());
        return paymentInfoDetail.get();
    }

    @Override
    public Object[] fetchPaymentInfo(Integer qrId, Integer tripId) {
        return infoRepo.fetchPaymentInfo(qrId, tripId);
    }

}
