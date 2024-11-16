package com.mdbackend.mdbackend.service.impl;

import java.util.Optional;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdbackend.mdbackend.entities.Bus;
import com.mdbackend.mdbackend.entities.Passenger;
import com.mdbackend.mdbackend.entities.PaymentInfo;
import com.mdbackend.mdbackend.entities.VehicleTrip;
import com.mdbackend.mdbackend.repo.BusRepo;
import com.mdbackend.mdbackend.repo.PassengerRepo;
import com.mdbackend.mdbackend.repo.PassengerTicketRepo;
import com.mdbackend.mdbackend.repo.PaymentInfoRepo;
import com.mdbackend.mdbackend.repo.VehicleQRRepo;
import com.mdbackend.mdbackend.repo.VehicleTripRepo;
import com.mdbackend.mdbackend.service.DiscountFareRateService;
import com.mdbackend.mdbackend.service.DistanceCalculationService;
import com.mdbackend.mdbackend.service.FareRateService;
import com.mdbackend.mdbackend.service.TicketQRService;
import com.mdbackend.mdbackend.service.VehicleTripService;
import com.mdbackend.mdbackend.entities.TicketQR;

@Service
public class VehicleTripServiceImpl implements VehicleTripService {

    @Autowired
    private VehicleTripRepo tripRepo;
    @Autowired
    private BusRepo busRepo;
    @Autowired
    private VehicleQRRepo qrRepo;
    @Autowired
    private DistanceCalculationService calculationService;
    @Autowired
    private FareRateService fareRateService;
    @Autowired
    private DiscountFareRateService dicountFareRateService;
    @Autowired
    private TicketQRService qrService;
    @Autowired
    private PaymentInfoRepo paymentInfoRepo;
    @Autowired
    private PassengerTicketRepo passengerTicketRepo;
    @Autowired
    private PassengerRepo passengerRepo;

    @Override
    public Integer tripCount(Integer busId) {
        return tripRepo.tripCount(busId);
    }

    @Override
    public Integer startVehicleTrip(Integer busId) throws Exception {
        VehicleTrip vehicleTrip = new VehicleTrip();
        Optional<Bus> bus = busRepo.findById(busId);
        if (bus.isPresent()) {
            vehicleTrip.setBusId(bus.get());
            tripRepo.save(vehicleTrip);
            return vehicleTrip.getId();
        } else {
            throw new Exception("Bus not found");
        }
    }

    @Override
    public void endVehicleTrip(Integer tripId) throws Exception {
        Optional<VehicleTrip> vehicleTrip = tripRepo.findById(tripId);
        if (vehicleTrip.isPresent()) {
            vehicleTrip.get().setActive(false);
            tripRepo.save(vehicleTrip.get());
        } else {
            throw new Exception("Trip not found");
        }
    }

    @Override
    public VehicleTrip vehicleTripId(Integer busId) {
        return tripRepo.vehicleTripId(busId);
    }

    @Override
    public void calculateFareForRemainingPassenger(Integer tripId) {

        List<TicketQR> ticketQRs = qrRepo.fetchTicketDataForTripEnd(tripId);
        if (ticketQRs == null || ticketQRs.isEmpty()) {
            return;
        }

        for (TicketQR ticketQR : ticketQRs) {
            PaymentInfo paymentInfo = new PaymentInfo();
            Optional<Bus> busOptional = busRepo.findById(ticketQR.getBusId());
            if (busOptional.isPresent()) {
                Bus bus = busOptional.get();
                paymentInfo.setBusId(bus);
            }

            calculationService.calculateDistanceUpToNow(ticketQR.getBusId(), tripId);
            Double totalDistance = Math
                    .round(calculationService.findTotalDistance(ticketQR.getSecretKey(), ticketQR.getBusId(),
                            tripId) * 100.0)
                    / 100.0;
            Double amount = 0.0;
            Double discountAmount = 0.0;
            if (totalDistance > 30) {
                amount = 55.0;
                discountAmount = 50.0;
            } else {
                amount = fareRateService.amount(totalDistance);
                discountAmount = dicountFareRateService.amount(totalDistance);
            }
            TicketQR passengerInfo = qrService.noOfPassenger(ticketQR.getSecretKey());
            Integer noOfPassengers = passengerInfo.getNumberOfPassengers();
            Integer noOfCardHolders = passengerInfo.getNumberOfCardHolders();
            Integer passengersWithoutDiscount = noOfPassengers - noOfCardHolders;

            Double fullAmountToPay = amount * passengersWithoutDiscount;
            Double discountedAmountToPay = discountAmount * noOfCardHolders;
            Double totalAmountToPay = fullAmountToPay + discountedAmountToPay;

            paymentInfo.setPayingAmount(totalAmountToPay);
            paymentInfo.setCreatedDate(new Date());
            paymentInfo.setDistanceTravel(totalDistance);
            paymentInfo.setNoOfCardHolder(noOfCardHolders);
            paymentInfo.setPassengerWithoutCard(passengersWithoutDiscount);
            paymentInfo.setPaymentStatus(0);
            paymentInfo.setQrId(ticketQR);
            Optional<VehicleTrip> tripOptional = tripRepo.findById(tripId);
            if (tripOptional.isPresent()) {
                VehicleTrip trip = tripOptional.get();
                paymentInfo.setTripId(trip);
            }
            Integer passengerId = passengerTicketRepo.fetchPassengerId(ticketQR.getId());
            if (passengerId != null) {
                Optional<Passenger> passengerOptional = passengerRepo.findById(passengerId);

                if (passengerOptional.isPresent()) {
                    Passenger passenger = passengerOptional.get();
                    paymentInfo.setPassengerId(passenger);
                }
            }

            paymentInfoRepo.save(paymentInfo);
        }
    }

}
