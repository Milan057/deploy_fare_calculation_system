package com.mdbackend.mdbackend.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mdbackend.mdbackend.entities.BusAdmin;

public interface BusAdminRepo extends JpaRepository<BusAdmin, Integer> {

    BusAdmin findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByShortName(String shortName);

    boolean existsByFullName(String fullName);

    @Query(value = "SELECT COUNT(*) AS totalOperators, " +
            "COUNT(CASE WHEN b.bus_admin_active = true THEN 1 END) AS activeOperators, " +
            "COUNT(CASE WHEN b.bus_admin_active = false THEN 1 END) AS inactiveOperators " +
            "FROM bus_admin b " +
            "WHERE b.del_flg = false", nativeQuery = true)
    Object[] fetchBusAdminStatus();

}
