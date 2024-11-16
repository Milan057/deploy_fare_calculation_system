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
@Table(name = "vehicle_trip")
public class VehicleTrip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JoinColumn(name = "bus_id", referencedColumnName = "id")
    @ManyToOne
    private Bus busId;
    @Column(name = "created_date")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "del_flg")
    private boolean delFlg=false;
    @Column(name = "active")
    private boolean active=true;
}
