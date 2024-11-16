package com.mdbackend.mdbackend.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mdbackend.mdbackend.entities.VehicleLocation;

public interface VehicleLocationRepo extends JpaRepository<VehicleLocation, Long> {
        @Query(value = "SELECT * from vehicle_location vl where vl.bus_id=:busId \n"
                        + "and vl.created_date BETWEEN "
                        + "(SELECT dc.to_time from distance_calculation\n"
                        + " dc WHERE dc.bus_id=:busId\n"
                        + "order by dc.to_time DESC\n"
                        + "LIMIT 1"
                        + ") \n and (SELECT vl.created_date WHERE vl.bus_id=:busId ORDER by vl.created_date DESC LIMIT 1)", nativeQuery = true)
        public List<VehicleLocation> fetchVehicleLocation(@Param("busId") Integer busId);

        @Query(value = "SELECT * " +
                        "FROM vehicle_location vl " +
                        "WHERE vl.created_date > ( " +
                        "    SELECT vt.created_date " +
                        "    FROM vehicle_trip vt " +
                        "    WHERE vt.active = true " +
                        "    AND vt.del_flg = false " +
                        "    AND vt.bus_id = ( " +
                        "        SELECT vt2.bus_id " +
                        "        FROM vehicle_trip vt2 " +
                        "        WHERE vt2.active = true " +
                        "        AND vt2.id = :id " +
                        "    ) " +
                        "    LIMIT 1 " +
                        ") " +
                        "AND vl.bus_id = ( " +
                        "    SELECT vt3.bus_id " +
                        "    FROM vehicle_trip vt3 " +
                        "    WHERE vt3.active = true " +
                        "    AND vt3.id = :id " +
                        ")", nativeQuery = true)
        List<VehicleLocation> fetchLocationsByTripId(@Param("id") Integer id);

}
