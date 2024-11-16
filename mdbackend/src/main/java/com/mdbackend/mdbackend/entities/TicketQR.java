package com.mdbackend.mdbackend.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class TicketQR {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "bus_id")
    private Integer busId;
    @Column(name = "qr_data")
    private String SecretKey;
    @Column(name = "del_flg")
    private boolean delFlg;
    @Column(name = "recorded_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date recordedTime;
    @Column(name = "number_Of_passengers")
    private Integer numberOfPassengers;
    @Column(name = "number_of_cardholders")
    private Integer numberOfCardHolders;
    @JoinColumn(name = "trip_id", referencedColumnName = "id")
    @ManyToOne
    private VehicleTrip tripId;
}
