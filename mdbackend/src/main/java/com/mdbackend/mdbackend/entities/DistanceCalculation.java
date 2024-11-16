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

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "distance_calculation")
public class DistanceCalculation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JoinColumn(name = "bus_id", referencedColumnName = "id")
    @ManyToOne
    private Bus busId;
    @Column(name = "from_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fromTime;
    @Column(name = "to_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date toTime;
    @Column(name = "distance")
    private Double distance;
    @JoinColumn(name = "trip_id", referencedColumnName = "id")
    @ManyToOne
    private VehicleTrip tripId;
}
