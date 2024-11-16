package com.mdbackend.mdbackend.entities;

import java.util.Date;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class EsewaTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "amount")
    private Double amount;
    @Column(name = "product_id")
    private String productId;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "del_flg")
    private boolean delFlg;
    @Column(name = "status")
    private String status;
    @Column(name = "ref_id")
    private String refId;
    @JoinColumn(name = "passenger_id", referencedColumnName = "id")
    @ManyToOne
    private Passenger passengerId;
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate; 
}
