package com.mdbackend.mdbackend.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mdbackend.mdbackend.entities.EsewaTransaction;

public interface EsewaTransactionRepo extends JpaRepository<EsewaTransaction, Integer> {
    @Query(value = "select \n"
            + "et.id as id,"
            + "et.amount as amount,\n"
            + "et.product_id  as productId\n"
            + "from esewa_transaction et \n"
            + "where et.del_flg=false\n"
            + "and et.status!='Completed'\n"
            + "and et.passenger_id=:passengerId", nativeQuery = true)
    public List<Object[]> fetchPendingEsewaTransaction(@Param("passengerId") Integer passengerId);

    @Query(value = "Select * from \n" +
            "esewa_transaction e\n" +
            "where e.del_flg=false\n" +
            "and e.passenger_id=:passengerId\n" +
            "and e.product_id=:productId\n" +
            "and e.status !='Completed'", nativeQuery = true)
    public EsewaTransaction fetchEsewaTransaction(@Param("passengerId") Integer passengerId,
            @Param("productId") String productId);
}
