package com.mdbackend.mdbackend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.mdbackend.mdbackend.dto.EsewaTransactionRequest;
import com.mdbackend.mdbackend.dto.EsewaTransactionResponse;
import com.mdbackend.mdbackend.dto.PassengerHistory;
import com.mdbackend.mdbackend.dto.PassengerTicketDTO;
import com.mdbackend.mdbackend.dto.PassengerTicketResponse;
import com.mdbackend.mdbackend.dto.PaymentInfoDTO;
import com.mdbackend.mdbackend.dto.PaymentRequest;
import com.mdbackend.mdbackend.dto.PaymentRequestDTO;
import com.mdbackend.mdbackend.dto.RequestForPayment;
import com.mdbackend.mdbackend.dto.ResponseDTO;
import com.mdbackend.mdbackend.dto.bus_dto.PassengerTicketStatus;
import com.mdbackend.mdbackend.dto.passenger_dto.PassengerDTO;
import com.mdbackend.mdbackend.dto.passenger_dto.PassengerLoginResponseDTO;
import com.mdbackend.mdbackend.dto.passenger_dto.PessangerDetailDTO;
import com.mdbackend.mdbackend.entities.PaymentInfo;
import com.mdbackend.mdbackend.entities.TicketQR;
import com.mdbackend.mdbackend.entities.models.CustomUserDetails;
import com.mdbackend.mdbackend.exceptions.ClientException;
import com.mdbackend.mdbackend.service.DiscountFareRateService;
import com.mdbackend.mdbackend.service.DistanceCalculationService;
import com.mdbackend.mdbackend.service.EsewaTransactionService;
import com.mdbackend.mdbackend.service.FareRateService;
import com.mdbackend.mdbackend.service.PassengerService;
import com.mdbackend.mdbackend.service.PassengerTicketService;
import com.mdbackend.mdbackend.service.PaymentInfoService;
import com.mdbackend.mdbackend.service.TicketQRService;
import com.mdbackend.mdbackend.service.UserBalanceService;
import com.mdbackend.mdbackend.service.impl.UserDetailServiceImp;
import com.mdbackend.mdbackend.utilis.JwtUtil;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/passenger")
public class PassengerController {

    @Autowired
    private PassengerService passengerService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailServiceImp userDetailServiceImp;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PassengerTicketService passengerTicketService;
    @Autowired
    private DistanceCalculationService calculationService;
    @Autowired
    private FareRateService fareRateService;
    @Autowired
    private DiscountFareRateService dicountFareRateService;
    @Autowired
    private TicketQRService qrService;
    @Autowired
    private PaymentInfoService infoService;
    @Autowired
    private UserBalanceService balanceService;
    @Autowired
    private EsewaTransactionService esewaTransactionService;

    @PostMapping("/register")
    public ResponseEntity<?> passengerRegister(@RequestBody PassengerDTO passenger) {
        if (passengerService.existsByEmail(passenger.getEmail())) {
            ResponseDTO responseDTO = new ResponseDTO("Email already exists", "EMAILALREADYEXISTS");
            return new ResponseEntity<>(responseDTO, HttpStatus.CONFLICT);
        }
        if (passengerService.existsByPhoneNumber(passenger.getPhoneNumber())) {
            ResponseDTO responseDTO = new ResponseDTO("Phone already exists", "PHONEALREADYEXISTS");
            return new ResponseEntity<>(responseDTO, HttpStatus.CONFLICT);
        }
        try {
            passengerService.savePassenger(passenger);
            return new ResponseEntity<>("Passenger register Sucessfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Server: An error occurred while saving the passenger",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody PassengerDTO user) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getPhoneNumber(), user.getPassword()));
            CustomUserDetails userDetails = (CustomUserDetails) userDetailServiceImp
                    .loadUserByUsername(user.getPhoneNumber());
            String jwt = jwtUtil.generateToken(userDetails);
            PessangerDetailDTO detailDTO = passengerService.getPassengerDetails(user.getPhoneNumber());
            Integer passengerId = detailDTO.getPassengerId();
            Object[] list = passengerService.fetchPassengerDetail(passengerId);
            Object[] innerStats = (Object[]) list[0];
            String name = innerStats[1].toString();
            String phone = innerStats[2].toString();
            String email = innerStats[3].toString();
            Double amount = innerStats[4] != null ? ((Number) innerStats[4]).doubleValue() : 0.0;
            return new ResponseEntity<>(new PassengerLoginResponseDTO(passengerId, name, phone, email, amount, jwt),
                    HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>("User not found!", HttpStatus.BAD_REQUEST);
        } catch (DisabledException e) {
            return new ResponseEntity<>("Sorry, you are inactive. Please contact the super admin.",
                    HttpStatus.BAD_REQUEST);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Invalid credentials!", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/savePassengerTicket")
    public ResponseEntity<?> savePassengerTicket(@RequestBody PassengerTicketDTO passenger_dto) {
        try {
            if (passengerTicketService.existsByQRId(passenger_dto.getQrData())) {
                return new ResponseEntity<>("Ticket Already Scanned", HttpStatus.CONFLICT);
            }
            PassengerTicketResponse response = passengerTicketService.savePassengerTicket(passenger_dto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (ClientException e) {
            return new ResponseEntity<>("Ticket not found",
                    HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/fetchPaymentInfo")
    public ResponseEntity<?> fetchPaymentInfo(@RequestBody PaymentRequest paymentRequest) {
        String qrData = paymentRequest.getQrData();
        Integer passengerId = paymentRequest.getPassengerId();
        Integer busId = paymentRequest.getBusId();
        if (qrData == null || passengerId == null || busId == null) {
            return new ResponseEntity<>("All fields are required.", HttpStatus.BAD_REQUEST);
        }
        try {
            Double availableAmount = balanceService.availableBalance(passengerId);
            Integer trip = qrService.fetchTripId(qrData);
            Integer qrId = qrService.findQrIdFromData(qrData);
            Object[] paymentInfoList = infoService.fetchPaymentInfo(qrId, trip);
            if (paymentInfoList != null && paymentInfoList.length > 0) {
                Object[] innerStats = (Object[]) paymentInfoList[0];
                Integer id = innerStats[0] != null ? ((Number) innerStats[0]).intValue() : 0;
                Double paymentAmount = innerStats[1] != null ? ((Number) innerStats[1]).doubleValue() : 0.0;
                Integer noOfcard = innerStats[2] != null ? ((Number) innerStats[2]).intValue() : 0;
                Integer noOfWithoutcard = innerStats[3] != null ? ((Number) innerStats[3]).intValue() : 0;
                Double distance = innerStats[4] != null ? ((Number) innerStats[4]).doubleValue() : 0.0;
                return new ResponseEntity<>(
                        new PaymentInfoDTO(paymentAmount, noOfWithoutcard, noOfcard, distance, id, availableAmount),
                        HttpStatus.OK);
            }
            calculationService.calculateDistanceUpToNow(paymentRequest.getBusId(), trip);
            Double totalDistance = Math.round(calculationService.findTotalDistance(qrData, busId, trip) * 100.0)
                    / 100.0;
            Double amount = 0.0;
            Double discountAmount = 0.0;
            System.out.println("total distance "+totalDistance);
            if (totalDistance > 30) {
                amount = 55.0;
                discountAmount = 50.0;
            } else {
                amount = fareRateService.amount(totalDistance);
                discountAmount = dicountFareRateService.amount(totalDistance);
            }
            TicketQR passengerInfo = qrService.noOfPassenger(qrData);
            if (passengerInfo == null) {
                return new ResponseEntity<>("Passenger information not found.", HttpStatus.NOT_FOUND);
            }

            Integer noOfPasenger = passengerInfo.getNumberOfPassengers();
            Integer noOfCard = passengerInfo.getNumberOfCardHolders();
            Integer passenegerWithNoDiscount = noOfPasenger - noOfCard;

            Double fullAmountToPay = amount * passenegerWithNoDiscount;
            Double discountedAmountToPay = discountAmount * noOfCard;
            Double totalAmountToPay = fullAmountToPay + discountedAmountToPay;

            Integer paymentInfo = infoService.savePaymentInfo(totalAmountToPay, trip, busId, passengerId, noOfCard,
                    passenegerWithNoDiscount, qrData, totalDistance);

            PaymentInfoDTO infoDTO = new PaymentInfoDTO(totalAmountToPay, passenegerWithNoDiscount, noOfCard,
                    totalDistance, paymentInfo, availableAmount);

            return new ResponseEntity<>(infoDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/payFare")
    public ResponseEntity<?> payFare(@RequestBody PaymentRequestDTO request) {
        try {
            if (request.getPassengerId() == null || request.getPaymentInfoId() == null) {
                return new ResponseEntity<>("Passenger ID and Payment Info ID must not be null",
                        HttpStatus.BAD_REQUEST);
            }
            PaymentInfo paymentInfo = infoService.returnPaymentInfoById(request.getPaymentInfoId());
            if (paymentInfo == null) {
                return new ResponseEntity<>("Payment Info not found", HttpStatus.NOT_FOUND);
            }
            Double availableAmount = balanceService.availableBalance(request.getPassengerId());
            if (paymentInfo.getPayingAmount() > availableAmount) {
                return new ResponseEntity<>("Insufficient balance. Please top up your balance.",
                        HttpStatus.PAYMENT_REQUIRED);
            }
            balanceService.reduceAmount(request.getPassengerId(), paymentInfo.getPayingAmount());
            return new ResponseEntity<>("Payment is done successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/history")
    public ResponseEntity<?> history(@RequestParam Integer passengerId) {
        try {
            List<PassengerHistory> passengerHistory = passengerTicketService.fetchHistory(passengerId);
            return new ResponseEntity<>(passengerHistory, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/fetchTicketStatus")
    public ResponseEntity<?> fetchTicketStatus(@RequestParam Integer passengerId) {
        try {
            Object[] stats = passengerTicketService.fetchTicketStatus(passengerId);
            Object[] innerStats = (Object[]) stats[0];
            Integer total = innerStats[0] != null ? ((Number) innerStats[0]).intValue() : 0;
            Integer active = innerStats[1] != null ? ((Number) innerStats[1]).intValue() : 0;
            Integer inactive = innerStats[2] != null ? ((Number) innerStats[2]).intValue() : 0;
            Double amount = (Double) innerStats[3];
            PassengerTicketStatus ticketStatus = new PassengerTicketStatus(total, active, inactive, amount);
            return new ResponseEntity<>(ticketStatus, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>("Server: Something went wrong!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/fetchTicketData")
    public ResponseEntity<?> fetchTicketData(@RequestParam String qrData) {
        PassengerTicketResponse response = passengerTicketService.fetchTicketDetail(qrData);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/esewaTransaction")
    public ResponseEntity<?> esewaTransaction(@RequestBody EsewaTransactionRequest entity) {
        try {
            EsewaTransactionResponse esewaTransactionResponse = esewaTransactionService
                    .savEsewaTransaction(entity.getPassengerId(), entity.getAmount());
            return new ResponseEntity<>(esewaTransactionResponse, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Server: Something went wrong!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/fetchPendingEsewaTransaction")
    public ResponseEntity<?> fetchPendingEsewaTransaction(@RequestParam Integer passengerId) {
        try {
            Double totalAmount = esewaTransactionService.esewaPay(passengerId);
            return new ResponseEntity<>(totalAmount, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Server: Something went wrong!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/esewaPayment")
    public ResponseEntity<?> esewaPayment(@RequestBody RequestForPayment requestForPayment) {
        try {
            Double amount = esewaTransactionService.esewaPayment(requestForPayment);
            return new ResponseEntity<>(amount, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Server: Something went wrong!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
