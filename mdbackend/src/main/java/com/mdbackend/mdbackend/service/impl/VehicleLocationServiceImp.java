package com.mdbackend.mdbackend.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdbackend.mdbackend.dto.VehicleLocationDTO;
import com.mdbackend.mdbackend.dto.VehicleLocationResponseDTO;
import com.mdbackend.mdbackend.entities.VehicleLocation;
import com.mdbackend.mdbackend.repo.VehicleLocationRepo;
import com.mdbackend.mdbackend.service.VehicleLocationService;
import com.mdbackend.mdbackend.utilis.DateConvertor;

@Service
public class VehicleLocationServiceImp implements VehicleLocationService {

    @Autowired
    private VehicleLocationRepo locationRepo;
    @Autowired
    private DateConvertor convertor;

    @Override
    public void saveVehicleLocation(VehicleLocationDTO dto) {
        VehicleLocation location = new VehicleLocation();
        location.setBusId(dto.getBusId());
        location.setCreatedDate(new Date());
        location.setLat(dto.getLat());
        location.setLon(dto.getLon());
        Date dateTime = convertor.convertToDate(dto.getRecordedTime());
        location.setRecordedTime(dateTime);

        locationRepo.save(location);
    }

    @Override
    public List<VehicleLocationResponseDTO> fetchVehicleLocation(Integer busId) {
        List<VehicleLocation> vehicleLocations = locationRepo.fetchLocationsByTripId(busId);
        List<VehicleLocationResponseDTO> dtos = new ArrayList<>();
        vehicleLocations.forEach(a -> {
            VehicleLocationResponseDTO dto = new VehicleLocationResponseDTO(a.getLat(), a.getLon());
            dtos.add(dto);
        });
        return dtos;
    }

}
