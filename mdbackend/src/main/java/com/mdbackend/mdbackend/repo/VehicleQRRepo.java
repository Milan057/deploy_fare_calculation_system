package com.mdbackend.mdbackend.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.mdbackend.mdbackend.entities.TicketQR;

public interface VehicleQRRepo extends JpaRepository<TicketQR, Integer> {
        @Query(value = "SELECT id FROM TicketQR WHERE qr_data = :qrData", nativeQuery = true)
        Integer findByQrData(@Param("qrData") String qrData);

        @Query(value = "SELECT * from ticketqr tqr where tqr.del_flg=false and tqr.qr_data=:qrData", nativeQuery = true)
        public TicketQR returnNumberofPasseneger(@Param("qrData") String qrData);

        @Query(value = "SELECT "
                        + "COUNT(tq.id) AS noOfTickets,\n"
                        + "SUM(CASE WHEN pi.payment_status = 1 THEN 1 ELSE 0 END) AS noOfCompletedPayments,\n"
                        + "SUM(CASE WHEN pi.payment_status =0 or pi.id is null THEN 1 ELSE 0 END) AS noOfPendingPayments\n"
                        + "FROM \n"
                        + " ticketqr tq\n"
                        + "LEFT JOIN \n"
                        + "  vehicle_trip vt ON vt.id = tq.trip_id\n"
                        + "LEFT JOIN \n"
                        + "  payment_info pi ON pi.qr_id = tq.id\n"
                        + "WHERE \n"
                        + "  vt.active = true \n"
                        + "AND tq.trip_id = :tripId \n", nativeQuery = true)
        Object[] fetchTicketStatus(@Param("tripId") Integer tripId);

        @Query(value = " select vt.id  from ticketqr tq left join vehicle_trip vt on vt.id=tq.trip_id where vt.del_flg=false and tq.qr_data=:qrData", nativeQuery = true)
        Integer fetchTripId(@Param("qrData") String qrData);

        @Query(value = "SELECT *\n"
                        + "FROM ticketqr t\n"
                        + "WHERE t.id  NOT IN (\n"
                        + "  SELECT p.qr_id\n"
                        + "FROM payment_info p\n"
                        + ")\n"
                        + "AND trip_id = :tripId\n", nativeQuery = true)
        List<TicketQR> fetchTicketDataForTripEnd(@Param("tripId") Integer tripId);

        @Query(value = "SELECT \n"
                        + "tq.trip_id AS tripId,\n"
                        + "tq.number_of_cardholders AS noOfCard,\n"
                        + "tq.number_of_passengers AS noOfPassenger,\n"
                        + "COALESCE(pt.scanned_time, '') AS scannedTime,\n"
                        + "COALESCE(p.full_name, '') AS passengerName,\n"
                        + "COALESCE(pi.paying_amount, '0.0') AS amount,\n"
                        + "COALESCE(pi.distance_travel, '0.0') AS distance,\n"
                        + "CASE\n"
                        + "  WHEN pt.ticket_qr_id IS NOT NULL THEN 'Scanned'\n"
                        + "   ELSE 'Not Scanned'\n"
                        + "END AS ticketStatus,\n"
                        + "CASE\n"
                        + "  WHEN pi.payment_status = '0' THEN 'Payment Pending'\n"
                        + "WHEN pi.payment_status = '1' THEN 'Payment Completed'\n"
                        + " ELSE 'No Payment Info'\n"
                        + "END AS paymentStatusDescription,\n"
                        +" tq.qr_data as qRData,\n"
                        +" tq.created_date as createdTime,\n"
                        +" tq.number_of_passengers as totalPassengers,\n"
                        +" tq.number_of_cardHolders as noOfCardHolders\n"
                        + "FROM ticketqr tq\n"
                        + "LEFT JOIN passenger_tickets pt ON pt.ticket_qr_id = tq.id\n"
                        + "LEFT JOIN passenger p ON p.id = pt.passenger_id\n"
                        + "LEFT JOIN payment_info pi ON pi.qr_id = tq.id\n"
                        + "WHERE \n"
                        + "tq.del_flg = FALSE \n"
                        + " AND (pi.del_flg = FALSE OR pi.del_flg IS NULL)\n"
                        + " AND (pt.del_flg = FALSE OR pt.del_flg IS NULL) \n"
                        + "AND (p.del_flg = FALSE OR p.del_flg IS NULL)\n"
                        + "AND tq.bus_id = :busId\n"
                        // + "AND tq.created_date BETWEEN CURRENT_DATE - INTERVAL 30 DAY AND CURRENT_DATE\n"
                        + "ORDER BY tq.created_date DESC\n", nativeQuery = true)
        List<Object[]> fetchHistoryForBus(@Param("busId") Integer busId);
}
