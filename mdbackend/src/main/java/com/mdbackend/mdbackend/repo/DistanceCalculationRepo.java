package com.mdbackend.mdbackend.repo;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mdbackend.mdbackend.entities.DistanceCalculation;

public interface DistanceCalculationRepo extends JpaRepository<DistanceCalculation, Integer> {
        @Query(value = "SELECT COALESCE(SUM(dc.distance), 0) AS total_distance "
                        + "FROM distance_calculation dc "
                        + "WHERE dc.to_time >  "
                        + "(SELECT tqr.created_date FROM ticketqr tqr "
                        + " WHERE tqr.qr_data = :qrData "
                        + " AND tqr.trip_id = :tripId "
                        + " AND tqr.del_flg = false "
                        + " AND tqr.bus_id = :busId) "
                        + "AND dc.to_time < :currentTime "
                        + "AND dc.trip_id = :tripId "
                        + "AND dc.bus_id = :busId", nativeQuery = true)
        public Double returnTotalDistance(@Param("qrData") String qrData,
                        @Param("busId") Integer busId,
                        @Param("tripId") Integer tripId,
                        @Param("currentTime") Date currentTime);

}
