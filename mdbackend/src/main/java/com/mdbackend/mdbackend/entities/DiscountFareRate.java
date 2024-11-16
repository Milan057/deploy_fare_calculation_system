package com.mdbackend.mdbackend.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "discount_fare_rate")
public class DiscountFareRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "from_distance")
    private Double fromDistance;
    @Column(name = "to_distance")
    private Double toDistance;
    @Column(name = "rate")
    private Double rate;
    @Column(name = "del_flg")
    private boolean delFlg = false;
}
