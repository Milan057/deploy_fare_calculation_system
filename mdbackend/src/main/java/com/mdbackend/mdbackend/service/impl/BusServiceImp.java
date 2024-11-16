package com.mdbackend.mdbackend.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mdbackend.mdbackend.dto.bus_dto.AllBusDetailDTO;
import com.mdbackend.mdbackend.dto.bus_dto.BusDTO;
import com.mdbackend.mdbackend.dto.bus_dto.BusDetailDTO;
import com.mdbackend.mdbackend.entities.Bus;
import com.mdbackend.mdbackend.enums.RoleEnum;
import com.mdbackend.mdbackend.repo.BusRepo;
import com.mdbackend.mdbackend.service.BusService;

@Service
public class BusServiceImp implements BusService {

  @Autowired
  private BusRepo busRepo;

  @Override
  public boolean existsByUsername(String userName) {
    return busRepo.existsByUserName(userName);
  }

  @Override
  public void saveBus(BusDTO busDTO) {
    Bus user = new Bus();
    user.setModifiedDate(new Date());
    user.setBusNumber(busDTO.getBusNumber());
    user.setUserName(busDTO.getUserName());
    user.setCreatedDate(new Date());
    user.setUserActive(true);
    user.setDelFlg(false);
    Set<String> roles = new HashSet<>();
    roles.add(RoleEnum.BUS.getRoleName());
    user.setRoles(roles);
    user.setBusAdmin(busDTO.getBusAdminId());
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    String encryptedPassword = bCryptPasswordEncoder.encode(busDTO.getPassword());
    user.setPassword(encryptedPassword);
    busRepo.save(user);
  }

  @Override
  public int existsByBusNumber(String busNumber, Integer busAdminId) {
    return busRepo.existSByBusNumber(busNumber, busAdminId);
  }

  @Override
  public BusDetailDTO getBusDetails(String userName) {
    Bus bus = busRepo.findByUserName(userName);
    if (bus != null) {
      return new BusDetailDTO(bus.getUserName(), bus.getId(), bus.getBusNumber());
    }
    throw new UsernameNotFoundException("User not found with username: " + userName);
  }

  @Override
  public Object[] fetchBusStatus(Integer busId) {
    return busRepo.fetchBusAdminStatus(busId);
  }

  @Override
  public List<BusDetailDTO> fetchActiveBus(Integer busAdmin) {
    List<Object[]> list = busRepo.fetchActiveBusList(busAdmin);
    List<BusDetailDTO> busDetailDTOs = new ArrayList<>();
    list.forEach(a -> {
      Integer busId = (Integer) a[0];
      String userName = a[1].toString();
      String busNumber = a[2].toString();
      BusDetailDTO busDetailDTO = new BusDetailDTO(userName, busId, busNumber);
      busDetailDTOs.add(busDetailDTO);
    });
    return busDetailDTOs;
  }

  @Override
  public List<AllBusDetailDTO> fetchAllBus(Integer busAdmin) {
    List<Object[]> list = busRepo.fetchAllBusList(busAdmin);
    List<AllBusDetailDTO> busDetailDTOs = new ArrayList<>();
    list.forEach(a -> {
      Integer busId = (Integer) a[0];
      String userName = a[1].toString();
      String busNumber = a[2].toString();
      boolean active = (boolean) a[3];
      AllBusDetailDTO busDetailDTO = new AllBusDetailDTO(userName, busId, busNumber, active);
      busDetailDTOs.add(busDetailDTO);
    });
    return busDetailDTOs;
  }

  @Override
  public void makeBusActive(Integer busId) throws Exception {
    Optional<Bus> bus = busRepo.findById(busId);
    if (bus.isPresent()) {
      bus.get().setUserActive(true);
      ;
      busRepo.save(bus.get());
    } else {
      throw new Exception("Bus not found");
    }
  }

  @Override
  public void makeBusInActive(Integer busId) throws Exception {
    Optional<Bus> bus = busRepo.findById(busId);
    if (bus.isPresent()) {
      bus.get().setUserActive(false);
      ;
      busRepo.save(bus.get());
    } else {
      throw new Exception("Bus not found");
    }
  }

}
