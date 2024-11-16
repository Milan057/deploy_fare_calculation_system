package com.mdbackend.mdbackend.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "payment_info")
public class PaymentInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "del_flg")
    private boolean delFlg = false;
    @Column(name = "paying_amount")
    private Double payingAmount;
    @Column(name = "payment_status")
    private Integer PaymentStatus = 0;
    @JoinColumn(name = "bus_id", referencedColumnName = "id")
    @ManyToOne
    private Bus busId;
    @JoinColumn(name = "trip_id", referencedColumnName = "id")
    @ManyToOne
    private VehicleTrip tripId;
    @JoinColumn(name = "passenger_id", referencedColumnName = "id")
    @ManyToOne
    private Passenger passengerId;
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdDate;
    @Column(name = "noOfCardHolder")
    private Integer noOfCardHolder;
    @Column(name = "passengerWithoutCard")
    private Integer passengerWithoutCard;
    @JoinColumn(name = "qr_id", referencedColumnName = "id")
    @ManyToOne
    private TicketQR qrId;
    @Column(name = "distance_travel")
    private double distanceTravel;

}
