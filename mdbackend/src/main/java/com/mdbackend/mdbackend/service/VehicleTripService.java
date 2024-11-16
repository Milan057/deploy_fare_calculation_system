package com.mdbackend.mdbackend.service;

import com.mdbackend.mdbackend.entities.VehicleTrip;

public interface VehicleTripService {
    public Integer tripCount(Integer busId);

    public Integer startVehicleTrip(Integer busId) throws Exception;

    public void endVehicleTrip(Integer tripId) throws Exception;
    public VehicleTrip vehicleTripId(Integer busId);
    public void calculateFareForRemainingPassenger(Integer tripId);

}
