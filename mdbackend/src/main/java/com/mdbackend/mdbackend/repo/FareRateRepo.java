package com.mdbackend.mdbackend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mdbackend.mdbackend.entities.FareRate;

public interface FareRateRepo extends JpaRepository<FareRate, Integer> {
    @Query(value = "SELECT * FROM fare_rate fr WHERE fr.del_flg = false AND :distance BETWEEN fr.from_distance AND fr.to_distance", nativeQuery = true)
    public FareRate findFareRateByDistance(@Param("distance") Double distance);

}
