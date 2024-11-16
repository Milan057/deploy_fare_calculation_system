package com.mdbackend.mdbackend.service;

import java.util.List;

import com.mdbackend.mdbackend.dto.bus_dto.AllBusDetailDTO;
import com.mdbackend.mdbackend.dto.bus_dto.BusDTO;
import com.mdbackend.mdbackend.dto.bus_dto.BusDetailDTO;

public interface BusService {
    public boolean existsByUsername(String userName);

    public void saveBus(BusDTO busDTO);

    public int existsByBusNumber(String busNumber, Integer busAdminId);

    public BusDetailDTO getBusDetails(String userName);

    public Object[] fetchBusStatus(Integer busId);

    public List<BusDetailDTO> fetchActiveBus(Integer busAdmin);

    public List<AllBusDetailDTO> fetchAllBus(Integer busAdmin);

    public void makeBusActive(Integer busId) throws Exception;

    public void makeBusInActive(Integer busId) throws Exception;
}
