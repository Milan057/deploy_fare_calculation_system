package com.mdbackend.mdbackend.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mdbackend.mdbackend.entities.SuperAdmin;

public interface SuperAdminRepo  extends JpaRepository<SuperAdmin,Integer> {
SuperAdmin findByEmail(String email);
}
