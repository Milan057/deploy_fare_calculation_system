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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mdbackend.mdbackend.dto.BusLoginResponse;
import com.mdbackend.mdbackend.dto.HistoryForBus;
import com.mdbackend.mdbackend.dto.TicketQRDTO;
import com.mdbackend.mdbackend.dto.TicketQRResponse;
import com.mdbackend.mdbackend.dto.VehicleLocationDTO;
import com.mdbackend.mdbackend.dto.VehicleLocationResponseDTO;
import com.mdbackend.mdbackend.dto.bus_dto.BusDTO;
import com.mdbackend.mdbackend.dto.bus_dto.BusDetailDTO;
import com.mdbackend.mdbackend.dto.bus_dto.TicketStatus;
import com.mdbackend.mdbackend.entities.VehicleTrip;
import com.mdbackend.mdbackend.entities.models.CustomUserDetails;
import com.mdbackend.mdbackend.exceptions.ClientException;
import com.mdbackend.mdbackend.service.BusService;
import com.mdbackend.mdbackend.service.DistanceCalculationService;
import com.mdbackend.mdbackend.service.TicketQRService;
import com.mdbackend.mdbackend.service.VehicleLocationService;
import com.mdbackend.mdbackend.service.VehicleTripService;
import com.mdbackend.mdbackend.service.impl.UserDetailServiceImp;
import com.mdbackend.mdbackend.service.impl.VehicleLocationServiceImp;
import com.mdbackend.mdbackend.utilis.JwtUtil;

@RestController
@RequestMapping("/bus")

public class BusController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailServiceImp userDetailServiceImp;
    @Autowired
    private BusService busService;
    @Autowired
    private VehicleLocationService locationService;
    @Autowired
    private TicketQRService qrdto;
    @Autowired
    private VehicleTripService vehicleTripService;
    @Autowired
    private VehicleLocationServiceImp locationServiceImp;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody BusDTO busAdmin) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(busAdmin.getUserName(), busAdmin.getPassword()));
            CustomUserDetails userDetails = (CustomUserDetails) userDetailServiceImp
                    .loadUserByUsername(busAdmin.getUserName().toLowerCase());
            String jwt = jwtUtil.generateToken(userDetails);
            BusDetailDTO busDetailDTO = busService.getBusDetails(busAdmin.getUserName().toLowerCase());
            Integer busId = busDetailDTO.getBusId();
            VehicleTrip trip = vehicleTripService.vehicleTripId(busId);
            BusLoginResponse response = new BusLoginResponse(jwt, busId, trip != null ? trip.getId() : null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>("User not found!", HttpStatus.BAD_REQUEST);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Invalid credentials!", HttpStatus.BAD_REQUEST);
        } catch (DisabledException e) {
            return new ResponseEntity<>("Sorry, you are inactive. Please contact the super admin.",
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Server: Something went worng!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/saveLiveLocation")
    public ResponseEntity<?> saveLiveLocation(@RequestBody VehicleLocationDTO locationDTO) {
        try {
            locationService.saveVehicleLocation(locationDTO);
            return new ResponseEntity<>("Location Save Sucessfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Server: An error occurred while saving the bus location",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/saveTickectQR")
    public ResponseEntity<?> saveTickectQR(@RequestBody TicketQRDTO ticketQRDTO) {
        try {
            if (ticketQRDTO.getTripId() == null) {
                throw new ClientException("You do not have an active Trip to create a ticket");
            }
            if (ticketQRDTO.getNumberOfPassengers() < 1) {
                return new ResponseEntity<>("Number of passenger can't be less than 1",
                        HttpStatus.BAD_REQUEST);
            }
            TicketQRResponse qrResponse = qrdto.saveTicketQR(ticketQRDTO);
            calculationService.calculateDistanceUpToNow(ticketQRDTO.getBusId(), ticketQRDTO.getTripId());
            return new ResponseEntity<>(qrResponse, HttpStatus.CREATED);
        } catch (ClientException e) {
            return new ResponseEntity<>("You do not have an active trip to create a ticket.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Server: An error occurred while saving ticket QR",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Autowired
    private VehicleTripService tripService;
    @Autowired
    private DistanceCalculationService calculationService;

    @PostMapping("/startVehicleTrip")
    public ResponseEntity<?> startVehicleTrip(@RequestBody Integer busId) {
        if (tripService.tripCount(busId) != 0) {
            return new ResponseEntity<>("Active Trip Exists", HttpStatus.CONFLICT);
        }
        try {
            Integer tripId = tripService.startVehicleTrip(busId);
            calculationService.saveStartingPoint(busId);
            return new ResponseEntity<>(tripId, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/endVehicleTrip")
    public ResponseEntity<?> endVehicleTrip(@RequestBody Integer tripId) {
        if (tripId == null) {
            return new ResponseEntity<>("Trip Id is missing", HttpStatus.BAD_REQUEST);
        }
        try {
            vehicleTripService.calculateFareForRemainingPassenger(tripId);
            tripService.endVehicleTrip(tripId);
            return new ResponseEntity<>("Trip Ended Successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/fetchTicketStatus")
    public ResponseEntity<?> fetchTicketStatus(@RequestParam Integer tripId) {
        try {
            Object[] stats = qrdto.fetchTicketStatus(tripId);
            Object[] innerStats = (Object[]) stats[0];
            Integer total = innerStats[0] != null ? ((Number) innerStats[0]).intValue() : 0;
            Integer active = innerStats[1] != null ? ((Number) innerStats[1]).intValue() : 0;
            Integer inactive = innerStats[2] != null ? ((Number) innerStats[2]).intValue() : 0;
            List<VehicleLocationResponseDTO> dtos = locationServiceImp.fetchVehicleLocation(tripId);
            TicketStatus ticketStatus = new TicketStatus(total, active, inactive, dtos);
            return new ResponseEntity<>(ticketStatus, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>("Server: Something went wrong!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/fetchHistory")
    public ResponseEntity<?> fetchHistory(@RequestParam Integer busId) {
        try {
            Map<Integer, List<HistoryForBus>> map = qrdto.fetchHistoryForBus(busId);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Server: Something went wrong!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
