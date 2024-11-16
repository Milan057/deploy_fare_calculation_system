package com.mdbackend.mdbackend.service;

import com.mdbackend.mdbackend.dto.bus_admin_dto.BusAdminDTO;
import com.mdbackend.mdbackend.dto.bus_admin_dto.BusAdminDetailDTO;

public interface BusAdminService {
    public void saveBusAdmin(BusAdminDTO busAdmin);

    public boolean existsByEmail(String email);

    public boolean existsByPhoneNumber(String phoneNumber);

    public BusAdminDetailDTO getBusAdminDetails(String phoneNumber);

    public boolean existsByShortName(String shortName);

    public boolean existsByFullName(String fullName);

    public String returnSortName(Integer id);

    public Object[] fetchBusAdminStatus();
}
