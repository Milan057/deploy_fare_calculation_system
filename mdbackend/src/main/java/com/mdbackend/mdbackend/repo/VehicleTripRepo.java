package com.mdbackend.mdbackend.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mdbackend.mdbackend.entities.VehicleTrip;

public interface VehicleTripRepo extends JpaRepository<VehicleTrip, Integer> {
    @Query(value = "select count(*)>0 from vehicle_trip vt where vt.bus_id=:busId and vt.del_flg=false and vt.active=true", nativeQuery = true)
    public Integer tripCount(@Param("busId") Integer busId);
    
    @Query(value = "Select * from vehicle_trip vt where vt.bus_id=:busId and vt.del_flg=false and vt.active=true limit 1",nativeQuery = true)
    public VehicleTrip vehicleTripId(@Param("busId") Integer busId);
}
