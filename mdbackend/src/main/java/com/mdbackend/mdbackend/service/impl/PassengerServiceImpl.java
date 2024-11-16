package com.mdbackend.mdbackend.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.mdbackend.mdbackend.dto.passenger_dto.PassengerDTO;
import com.mdbackend.mdbackend.dto.passenger_dto.PessangerDetailDTO;
import com.mdbackend.mdbackend.entities.Passenger;
import com.mdbackend.mdbackend.enums.RoleEnum;
import com.mdbackend.mdbackend.repo.PassengerRepo;
import com.mdbackend.mdbackend.service.PassengerService;

@Service
public class PassengerServiceImpl implements PassengerService {
    @Autowired
    private PassengerRepo passRepo;

    public void savePassenger(PassengerDTO passenger) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encriptPassword = bCryptPasswordEncoder.encode(passenger.getPassword());
        Passenger p = new Passenger();
        p.setEmail(passenger.getEmail());
        p.setFullName(passenger.getFullName());
        p.setPhoneNumber(passenger.getPhoneNumber());
        p.setPassword(encriptPassword);
        p.setDelFlg(false);
        p.setUserActive(true);
        p.setModifiedDate(new Date());
        p.setCreatedDate(new Date());
        Set<String> roles = new HashSet<>();
        roles.add(RoleEnum.PASSENGER.getRoleName());
        p.setRoles(roles);
        passRepo.save(p);
    }

    @Override
    public boolean existsByEmail(String email) {
        return passRepo.existsByEmail(email);
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return passRepo.existsByPhoneNumber(phoneNumber);
    }

    public PessangerDetailDTO getPassengerDetails(String phoneNumber) {
        Passenger passenger = passRepo.findByPhoneNumber(phoneNumber);
        if (passenger != null) {
            return new PessangerDetailDTO(passenger.getId(), passenger.getFullName(), passenger.getPhoneNumber(),
                    passenger.getEmail());
        }
        throw new UsernameNotFoundException("User not found with username: " + phoneNumber);
    }

    @Override
    public Object[] fetchPassengerDetail(Integer passengerId) {
        return passRepo.fetchPassengerDetail(passengerId);

    }

}
