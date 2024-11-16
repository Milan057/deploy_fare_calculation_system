package com.mdbackend.mdbackend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdbackend.mdbackend.dto.ResponseDTO;
import com.mdbackend.mdbackend.dto.bus_admin_dto.BusAdminDTO;
import com.mdbackend.mdbackend.dto.bus_admin_dto.BusAdminStatus;
import com.mdbackend.mdbackend.dto.super_admin_dto.SuperAdminDTO;
import com.mdbackend.mdbackend.entities.models.CustomUserDetails;
import com.mdbackend.mdbackend.service.impl.BusAdminServiceImp;
import com.mdbackend.mdbackend.service.impl.UserDetailServiceImp;
import com.mdbackend.mdbackend.utilis.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/superAdmin")
public class SuperAdminController {
    @Autowired
    private BusAdminServiceImp busAdminServiceImp;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailServiceImp userDetailServiceImp;

    @PostMapping("/login")
    public ResponseEntity<?> Login(@RequestBody SuperAdminDTO superAdmin) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(superAdmin.getEmail(), superAdmin.getPassword()));
            CustomUserDetails userDetails = (CustomUserDetails) userDetailServiceImp
                    .loadUserByUsername(superAdmin.getEmail());

            String jwt = jwtUtil.generateToken(userDetails);
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>("User not found!", HttpStatus.BAD_REQUEST);
        } catch (DisabledException e) {
            return new ResponseEntity<>("Sorry, you are inactive. Please contact the super admin.",
                    HttpStatus.BAD_REQUEST);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Invalid credentials!", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Server: Something went worng!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/saveBusAdmin")
    public ResponseEntity<?> saveBusAdmin(@RequestBody BusAdminDTO busAdminDTO) {
        if (busAdminServiceImp.existsByEmail(busAdminDTO.getEmail())) {
            ResponseDTO responseDTO = new ResponseDTO("Email already exists", "EMAILALREADYEXISTS");
            return new ResponseEntity<>(responseDTO, HttpStatus.CONFLICT);
        }
        if (busAdminServiceImp.existsByPhoneNumber(busAdminDTO.getPhoneNumber())) {
            ResponseDTO responseDTO = new ResponseDTO("Phone already exists", "PHONEALREADYEXISTS");
            return new ResponseEntity<>(responseDTO, HttpStatus.CONFLICT);
        }
        if (busAdminServiceImp.existsByShortName(busAdminDTO.getShortName())) {
            ResponseDTO responseDTO = new ResponseDTO("Short name already exists", "SHORTNAMEALREADYEXISTS");
            return new ResponseEntity<>(responseDTO, HttpStatus.CONFLICT);
        }
        if (busAdminServiceImp.existsByFullName(busAdminDTO.getFullName())) {
            ResponseDTO responseDTO = new ResponseDTO("Full name already exists", "FULLNAMEALREADYEXISTS");
            return new ResponseEntity<>(responseDTO, HttpStatus.CONFLICT);
        }
        try {
            busAdminServiceImp.saveBusAdmin(busAdminDTO);
            return new ResponseEntity<>("Bus Admin added Sucessfully", HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>("Server: An error occurred while saving the bus admin",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/fetchBusAdminStatus")
    public ResponseEntity<?> fetchBusAdminStatus() {
        try {
            Object[] stats = busAdminServiceImp.fetchBusAdminStatus();
            Object[] innerStats = (Object[]) stats[0];
            Integer total = innerStats[0] != null ? ((Number) innerStats[0]).intValue() : 0;
            Integer active = innerStats[1] != null ? ((Number) innerStats[1]).intValue() : 0;
            Integer inactive = innerStats[2] != null ? ((Number) innerStats[2]).intValue() : 0;
            BusAdminStatus busAdminStatus = new BusAdminStatus(total, active, inactive);
            return new ResponseEntity<>(busAdminStatus, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>("Server: Something went wrong!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
