package com.mdbackend.mdbackend.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "passenger_tickets")
public class PassengerTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "ticket_qr_id")
    private Integer ticketQrId;
    @Column(name = "del_flg")
    private boolean delFlg;
    @Column(name = "scanned_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date scannedTime;
    @Column(name = "passenger_id")
    private Integer passengerId;

}
