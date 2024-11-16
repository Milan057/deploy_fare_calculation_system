package com.mdbackend.mdbackend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.mdbackend.mdbackend.entities.UserBalance;

public interface UserBalanceRepo extends JpaRepository<UserBalance, Integer> {
    @Query(value = "SELECT * FROM user_balance ub WHERE ub.del_flg = false AND ub.passenger_id = :passengerId", nativeQuery = true)
    public UserBalance returnAvailableAmount(@Param("passengerId") Integer passengerId);

    
}
