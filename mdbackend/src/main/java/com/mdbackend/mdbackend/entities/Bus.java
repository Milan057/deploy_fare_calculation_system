package com.mdbackend.mdbackend.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Bus implements AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", unique = true, nullable = false)
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "del_flg")
    private boolean delFlg;

    @Column(name = "bus_active")
    private boolean userActive;

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "bus_number")
    private String busNumber;

    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @ElementCollection
    @CollectionTable(name = "bus_roles", joinColumns = @JoinColumn(name = "bus_id"))
    @Column(name = "role")
    private Set<String> roles;

    @JoinColumn(name = "bus_admin_id", referencedColumnName = "id")
    private Integer busAdmin;

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public boolean getDelFlg() {
        return delFlg;
    }

    @Override
    public boolean getUserActive() {
    return userActive;
    }

}
