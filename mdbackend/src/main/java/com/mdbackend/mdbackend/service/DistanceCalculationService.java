package com.mdbackend.mdbackend.service;

public interface DistanceCalculationService {
    public void calculateDistanceUpToNow(Integer busId, Integer tripId);

    public void saveStartingPoint(Integer busId);

    public Double findTotalDistance(String qrData,Integer busId, Integer tripId);
}
