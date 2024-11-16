package com.mdbackend.mdbackend.service.impl;

import com.mdbackend.mdbackend.enums.EmailSendStatus;
import com.mdbackend.mdbackend.dto.bus_admin_dto.BusAdminDTO;
import com.mdbackend.mdbackend.dto.bus_admin_dto.BusAdminDetailDTO;
import com.mdbackend.mdbackend.entities.BusAdmin;
import com.mdbackend.mdbackend.enums.RoleEnum;
import com.mdbackend.mdbackend.repo.BusAdminRepo;
import com.mdbackend.mdbackend.service.BusAdminService;
import com.mdbackend.mdbackend.utilis.Generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class BusAdminServiceImp implements BusAdminService {

    @Autowired
    private BusAdminRepo busAdminRepo;
    @Autowired
    private Generator passwordGenerator;
    @Autowired
    private EmailServiceImp emailServiceImp;

    @Override
    public void saveBusAdmin(BusAdminDTO busAdmin) {
        try {
            BusAdmin user = new BusAdmin();
            user.setCreatedDate(new Date());
            user.setModifiedDate(new Date());
            user.setFullName(busAdmin.getFullName());
            user.setPhoneNumber(busAdmin.getPhoneNumber());
            user.setEmail(busAdmin.getEmail());
            user.setDelFlg(false);
            user.setUserActive(true);
            user.setShortName(busAdmin.getShortName());
            Set<String> roles = new HashSet<>();
            roles.add(RoleEnum.BUS_ADMIN.getRoleName());
            user.setRoles(roles);
            String password = passwordGenerator.generateRandomPassword(6);
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String encryptedPassword = bCryptPasswordEncoder.encode(password);
            user.setPassword(encryptedPassword);

            EmailSendStatus emailSendStatus = EmailSendStatus.TRUE;

            if (emailSendStatus == EmailSendStatus.TRUE) {
                String subject = "Account Created";
                String text = "Dear " + busAdmin.getFullName()
                        + ",\n\nYour account has been created.\nYour temporary password is: " + password +
                        "\n\nPlease do not share it with others and change your password after logging in.\nThank you!\nYatriBhada";

                boolean emailSent = emailServiceImp.sendEmail(busAdmin.getEmail(), subject, text);
                if (emailSent) {
                    busAdminRepo.save(user);
                } else {
                    throw new RuntimeException("Failed to send email. User creation aborted.");
                }
            } else {
                busAdminRepo.save(user);
            }

        } catch (Exception ex) {
            throw new RuntimeException("Failed to save user due to an unexpected error: " + ex.getMessage(), ex);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        return busAdminRepo.existsByEmail(email);
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return busAdminRepo.existsByPhoneNumber(phoneNumber);

    }

    @Override
    public BusAdminDetailDTO getBusAdminDetails(String phoneNumber) {
        BusAdmin busAdmin = busAdminRepo.findByEmail(phoneNumber);
        if (busAdmin != null) {
            return new BusAdminDetailDTO(busAdmin.getId(), busAdmin.getFullName(), busAdmin.getPhoneNumber(),
                    busAdmin.getEmail(), busAdmin.getShortName());
        }
        throw new UsernameNotFoundException("User not found with username: " + phoneNumber);
    }

    @Override
    public boolean existsByShortName(String shortName) {
        return busAdminRepo.existsByShortName(shortName);
    }

    @Override
    public boolean existsByFullName(String fullName) {
        return busAdminRepo.existsByFullName(fullName);
    }

    @Override
    public String returnSortName(Integer id) {
        return busAdminRepo.findById(id)
                .map(BusAdmin::getShortName)
                .orElseThrow(() -> new UsernameNotFoundException("BusAdmin not found with id: " + id));
    }

    @Override
    public Object[] fetchBusAdminStatus() {
        return busAdminRepo.fetchBusAdminStatus();
    }
}
