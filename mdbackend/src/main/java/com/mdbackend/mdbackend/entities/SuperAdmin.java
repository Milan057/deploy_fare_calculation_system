package com.mdbackend.mdbackend.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SuperAdmin  implements AppUser{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone_number", unique = true, nullable = false)
    private String phoneNumber;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "del_flg")
    private boolean delFlg;
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
    @ElementCollection
    @CollectionTable(name = "super_admin_role", joinColumns = @JoinColumn(name = "super_admin_id"))
    @Column(name = "role")
    private Set<String> roles;
    @Override
    public String getUserName() {
        return email;
    }
    @Override
    public boolean getDelFlg() {
        return delFlg;
    }
    @Override
    public boolean getUserActive() {
        return true;
    }
 

}