package com.mdbackend.mdbackend.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mdbackend.mdbackend.entities.Passenger;

public interface PassengerRepo extends JpaRepository<Passenger, Integer> {
    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    Passenger findByPhoneNumber(String phoneNumber);

    @Query(value = "SELECT p.id as passengerId,\n"
            + "p.full_name as fullName,\n"
            + "p.phone_number as phoneNumber,\n"
            + "p.email as email,\n"
            + "ub.available_balance as balance\n"
            + "FROM passenger p\n"
            + "LEFT join user_balance ub\n"
            + "on ub.passenger_id=p.id\n"
            + "where\n"
            + "p.id=:passengerId and\n"
            + "p.del_flg=false and\n"
            + "p.passenger_active=true", nativeQuery = true)
    public Object[] fetchPassengerDetail(@Param("passengerId") Integer passengerId);
}
