package com.mdbackend.mdbackend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mdbackend.mdbackend.entities.PaymentInfo;

public interface PaymentInfoRepo extends JpaRepository<PaymentInfo, Integer> {
    @Query(value = "select p.id,p.paying_amount as amount,p.no_of_card_holder as noOfCardHolder,p.passenger_without_card as passnegerWithoutCard,p.distance_travel as distanceTravel from payment_info p where p.qr_id =:qrId and p.trip_id=:tripId", nativeQuery = true)
    Object[] fetchPaymentInfo(@Param("qrId") Integer qrId, @Param("tripId") Integer tripId);

}
