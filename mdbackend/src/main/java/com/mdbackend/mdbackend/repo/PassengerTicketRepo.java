package com.mdbackend.mdbackend.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mdbackend.mdbackend.entities.PassengerTicket;

public interface PassengerTicketRepo extends JpaRepository<PassengerTicket, Integer> {
        @Query(value = "SELECT b.bus_number, b.id, tq.recorded_time, tq.number_of_passengers, tq.number_of_cardholders ,tq.trip_id \n"
                        +
                        "FROM passenger_tickets pt " +
                        "LEFT JOIN ticketqr tq ON pt.ticket_qr_id = tq.id " +
                        "LEFT JOIN bus b ON b.id = tq.bus_id " +
                        "WHERE pt.id = :qrId AND tq.del_flg = false", nativeQuery = true)
        List<Object[]> returnTicketResponse(@Param("qrId") Integer qrId);

        boolean existsByTicketQrId(Integer ticketQrId);

        @Query(value = "Select \n"
                        + "tqr.created_date as ticketCreatedDate,\n"
                        + "  COALESCE(pi.payment_status, 0) AS paymentStatus,\n"
                        + "COALESCE(pi.paying_amount, 0.0) AS paidAmount,\n"
                        + " tqr.number_of_cardholders as cardHolders,\n"
                        + " tqr.number_of_passengers as noOfPassenger,  tqr.qr_data AS qrData from\n"
                        + " passenger_tickets pt \n"
                        + "left JOIN passenger p\n"
                        + "on p.id= pt.passenger_id left join\n"
                        + " payment_info pi ON pi.passenger_id = p.id AND pi.qr_id = pt.ticket_qr_id \n"
                        + "left join ticketqr tqr \n"
                        + "on tqr.id= pt.ticket_qr_id \n"
                        + "where pt.passenger_id=:passengerId \n"
                        + "and p.del_flg=false and pi.del_flg=false and p.passenger_active=true ", nativeQuery = true)
        public List<Object[]> returnPassnegerHistory(@Param("passengerId") Integer passengerId);

        @Query(value = "SELECT COUNT(pt.id) AS noOfTickets,\n"
                        + "SUM(CASE WHEN pi.payment_status = 1 THEN 1 ELSE 0 END) AS noOfCompletedPayments,\n"
                        + "SUM(CASE WHEN pi.payment_status =0 or pi.id is null THEN 1 ELSE 0 END) AS noOfPendingPayments,\n"
                        + "IfNull((Select ub.available_balance from user_balance ub where ub.passenger_id=:passengerId),0.0) as amount \n"
                        + "FROM passenger_tickets pt\n"
                        + " LEFT JOIN \n"
                        + "ticketqr tq ON tq.id = pt.ticket_qr_id\n"
                        + "LEFT JOIN \n"
                        + "payment_info pi ON pi.qr_id = tq.id\n"
                        + "left join passenger p\n"
                        + "on p.id=pt.passenger_id\n"
                        + " WHERE p.id=:passengerId and p.del_flg=false and p.passenger_active=true and pi.del_flg=false \n", nativeQuery = true)
        Object[] fetchTicketStatus(@Param("passengerId") Integer passengerId);

        @Query(value = "SELECT b.bus_number, b.id, tq.recorded_time, tq.number_of_passengers, tq.number_of_cardholders ,tq.trip_id \n"
                        +
                        "FROM passenger_tickets pt " +
                        "LEFT JOIN ticketqr tq ON pt.ticket_qr_id = tq.id " +
                        "LEFT JOIN bus b ON b.id = tq.bus_id " +
                        "WHERE tq.id = :qrId AND tq.del_flg = false", nativeQuery = true)
        List<Object[]> returnPaymentResponse(@Param("qrId") Integer qrId);

        @Query(value = "SELECT p.passenger_id FROM passenger_tickets p WHERE p.ticket_qr_id = :qrId", nativeQuery = true)
        public Integer fetchPassengerId(@Param("qrId") Integer qrId);

}
