package com.mdbackend.mdbackend.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mdbackend.mdbackend.entities.Bus;

public interface BusRepo extends JpaRepository<Bus, Integer> {
  Bus findByUserName(String userName);

  Boolean existsByUserName(String userName);

  @Query(value = "SELECT count(*)>0  FROM bus WHERE bus_number = :busNumber and bus_admin= :bus_admin", nativeQuery = true)
  int existSByBusNumber(@Param("busNumber") String busNumber, @Param("bus_admin") Integer busAdminId);

  @Query(value = "SELECT COUNT(*) AS totalVehicle, " +
      "COUNT(CASE WHEN b.bus_active = true THEN 1 END) AS activeBus, " +
      "COUNT(CASE WHEN b.bus_active = false THEN 1 END) AS inactiveBus " +
      "FROM bus b " +
      "WHERE b.del_flg = false AND b.bus_admin = :busId", nativeQuery = true)
  Object[] fetchBusAdminStatus(@Param("busId") Integer busId);

  @Query(value = "SELECT " +
      "b.id as id," +
      "b.username as userName, " +
      "b.bus_number as busNumber " +
      "from bus b " +
      "where b.bus_active=true " +
      "and b.del_flg=false " +
      "and b.bus_admin=:busAdmin", nativeQuery = true)
  List<Object[]> fetchActiveBusList(@Param("busAdmin") Integer busAdmin);

  @Query(value = "SELECT " +
      "b.id as id," +
      "b.username as userName, " +
      "b.bus_number as busNumber," +
      "b.bus_active as active "+
      "from bus b " +
      "where b.del_flg=false " +
      "and b.bus_admin=:busAdmin", nativeQuery = true)
  List<Object[]> fetchAllBusList(@Param("busAdmin") Integer busAdmin);
}
