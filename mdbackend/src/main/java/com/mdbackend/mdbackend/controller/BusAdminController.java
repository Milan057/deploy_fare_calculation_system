package com.mdbackend.mdbackend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mdbackend.mdbackend.dto.HistoryForBus;
import com.mdbackend.mdbackend.dto.LoginResponse;
import com.mdbackend.mdbackend.dto.ResponseDTO;
import com.mdbackend.mdbackend.dto.bus_admin_dto.BusAdminDTO;
import com.mdbackend.mdbackend.dto.bus_admin_dto.BusAdminDetailDTO;
import com.mdbackend.mdbackend.dto.bus_admin_dto.BusAdminStatus;
import com.mdbackend.mdbackend.dto.bus_dto.AllBusDetailDTO;
import com.mdbackend.mdbackend.dto.bus_dto.BusDTO;
import com.mdbackend.mdbackend.dto.bus_dto.BusDetailDTO;
import com.mdbackend.mdbackend.entities.models.CustomUserDetails;
import com.mdbackend.mdbackend.service.BusAdminService;
import com.mdbackend.mdbackend.service.BusService;
import com.mdbackend.mdbackend.service.TicketQRService;
import com.mdbackend.mdbackend.service.impl.UserDetailServiceImp;
import com.mdbackend.mdbackend.utilis.JwtUtil;

@RestController
@RequestMapping("/busAdmin")
public class BusAdminController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailServiceImp userDetailServiceImp;
    @Autowired
    private BusService busService;
    @Autowired
    private BusAdminService adminService;
    @Autowired
    private TicketQRService ticketQRService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody BusAdminDTO busAdmin) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(busAdmin.getEmail(), busAdmin.getPassword()));

            CustomUserDetails userDetails = (CustomUserDetails) userDetailServiceImp
                    .loadUserByUsername(busAdmin.getEmail());

            String jwt = jwtUtil.generateToken(userDetails);
            BusAdminDetailDTO adminDetailDTO = adminService.getBusAdminDetails(busAdmin.getEmail());
            Integer busAdminId = adminDetailDTO.getBusAdminId();

            LoginResponse response = new LoginResponse(jwt, busAdminId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>("User not found!", HttpStatus.BAD_REQUEST);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Invalid credentials!", HttpStatus.BAD_REQUEST);
        } catch (DisabledException e) {
            return new ResponseEntity<>("Sorry, you are inactive. Please contact the super admin.",
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>("Server: Something went wrong!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/saveBus")
    public ResponseEntity<?> saveBus(@RequestBody BusDTO busDTO) {
        String userName = busDTO.getUniqueName() + "@" + adminService.returnSortName(busDTO.getBusAdminId()) + ".com";
        busDTO.setUserName(userName.toLowerCase());
        if (busService.existsByUsername(busDTO.getUserName())) {
            ResponseDTO responseDTO = new ResponseDTO("Bus Unique Name Already Exists", "BUSUNIQUENAMEALREADYEXISTS");
            return new ResponseEntity<>(responseDTO, HttpStatus.CONFLICT);
        }
        if (busService.existsByBusNumber(busDTO.getBusNumber(), busDTO.getBusAdminId()) > 0) {
            ResponseDTO responseDTO = new ResponseDTO("Vehicle Number Already exists", "VEHICLENUMBERALREADYEXISTS");
            return new ResponseEntity<>(responseDTO, HttpStatus.CONFLICT);
        }

        try {
            busService.saveBus(busDTO);
            return new ResponseEntity<>("Bus added Sucessfully", HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>("Server: An error occurred while saving the bus",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/user-details")
    public ResponseEntity<?> getBusAdminDetails(@RequestHeader("Authorization") String token) {
        try {
            String username = jwtUtil.extractUsername(token.substring(7));
            BusAdminDetailDTO adminDetailDTO = adminService.getBusAdminDetails(username);
            return new ResponseEntity<>(adminDetailDTO, HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>("Server: Something went worng!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/fetchBusStatus")
    public ResponseEntity<?> fetchBusStatus(@RequestParam Integer busAdminId) {
        try {
            Object[] stats = busService.fetchBusStatus(busAdminId);
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

    @GetMapping("/fetchActiveBus")
    public ResponseEntity<?> fetchActiveBus(@RequestParam Integer busAdmin) {
        try {
            List<BusDetailDTO> list = busService.fetchActiveBus(busAdmin);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Server: Something went wrong!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/fetchHistory")
    public ResponseEntity<?> fetchHistory(@RequestParam Integer busId) {
        try {
            Map<Integer, List<HistoryForBus>> map = ticketQRService.fetchHistoryForBus(busId);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Server: Something went wrong!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/fetchAllBusList")
    public ResponseEntity<?> fetchAllBusList(@RequestParam Integer busAdmin) {
        try {
            List<AllBusDetailDTO> list = busService.fetchAllBus(busAdmin);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Server: Something went wrong!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("makeBusActive")
    public ResponseEntity<?> makeBusActive(@RequestParam Integer busId) {
        try {
            busService.makeBusActive(busId);
            return new ResponseEntity<>("Operation Sucessful", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Server: Something went wrong!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("makeBusInActive")
    public ResponseEntity<?> makeBusInActive(@RequestParam Integer busId) {
        try {
            busService.makeBusInActive(busId);
            return new ResponseEntity<>("Operation Sucessful", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Server: Something went wrong!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
