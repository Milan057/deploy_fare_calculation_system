package com.mdbackend.mdbackend.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdbackend.mdbackend.entities.Bus;
import com.mdbackend.mdbackend.entities.DistanceCalculation;
import com.mdbackend.mdbackend.entities.VehicleLocation;
import com.mdbackend.mdbackend.repo.BusRepo;
import com.mdbackend.mdbackend.repo.DistanceCalculationRepo;
import com.mdbackend.mdbackend.repo.VehicleLocationRepo;
import com.mdbackend.mdbackend.repo.VehicleTripRepo;
import com.mdbackend.mdbackend.service.DistanceCalculationService;

@Service
public class DistanceCalculationServiceImpl implements DistanceCalculationService {

    @Autowired
    private VehicleLocationRepo locationRepo;
    @Autowired
    private DistanceCalculationRepo calculationRepo;
    @Autowired
    private VehicleTripRepo vehicleTripRepo;
    @Autowired
    private BusRepo busRepo;

    @Override
    public void calculateDistanceUpToNow(Integer busId, Integer tripId) {
        List<VehicleLocation> vehicleLocationList = locationRepo.fetchVehicleLocation(busId);
        if (vehicleLocationList == null || vehicleLocationList.size() <= 1) {
            return;
        }
        Double totalDistance = 0.0;
        for (int i = 0; i < vehicleLocationList.size(); i++) {
            VehicleLocation vh1 = null, vh2 = null;
            vh1 = vehicleLocationList.get(i);
            try {
                vh2 = vehicleLocationList.get(i + 1);
            } catch (IndexOutOfBoundsException e) {
                break;
            }
            totalDistance += haversine(Double.parseDouble(vh1.getLat()),
                    Double.parseDouble(vh1.getLon()),
                    Double.parseDouble(vh2.getLat()), Double.parseDouble(vh2.getLon()));
        }
        DistanceCalculation calculation = new DistanceCalculation();
        Optional<Bus> bus = busRepo.findById(busId);
        if (bus.isPresent()) {
            calculation.setBusId(bus.get());
        }
        calculation.setTripId(vehicleTripRepo.vehicleTripId(busId));
        calculation.setDistance(totalDistance);
        calculation.setFromTime(vehicleLocationList.get(0).getCreatedDate());
        calculation.setToTime(vehicleLocationList.get(vehicleLocationList.size() -
                1).getCreatedDate());
        calculationRepo.save(calculation);
    }

    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;

        double a = Math.pow(Math.sin(dLat / 2), 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return 6371 * c;
    }

    @Override
    public void saveStartingPoint(Integer busId) {
        DistanceCalculation calculation = new DistanceCalculation();
        Optional<Bus> bus = busRepo.findById(busId);
        if (bus.isPresent()) {
            calculation.setBusId(bus.get());
        }
        calculation.setTripId(vehicleTripRepo.vehicleTripId(busId));
        calculation.setDistance(0.0);
        calculation.setToTime(new Date());
        calculation.setFromTime(new Date());
        calculationRepo.save(calculation);
    }

    @Override
    public Double findTotalDistance(String qrData, Integer busId, Integer tripId) {
        return calculationRepo.returnTotalDistance(qrData, busId, tripId, new Date());
    }

}
