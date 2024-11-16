package com.mdbackend.mdbackend.service;

import java.util.List;

import com.mdbackend.mdbackend.dto.VehicleLocationDTO;
import com.mdbackend.mdbackend.dto.VehicleLocationResponseDTO;

public interface VehicleLocationService {
    public void saveVehicleLocation(VehicleLocationDTO dto);

    public List<VehicleLocationResponseDTO> fetchVehicleLocation(Integer busId);

}
