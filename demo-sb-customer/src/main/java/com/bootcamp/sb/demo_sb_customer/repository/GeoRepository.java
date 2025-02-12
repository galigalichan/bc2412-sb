package com.bootcamp.sb.demo_sb_customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bootcamp.sb.demo_sb_customer.entity.GeoEntity;

@Repository
public interface GeoRepository extends JpaRepository<GeoEntity, Long> {
    
}
