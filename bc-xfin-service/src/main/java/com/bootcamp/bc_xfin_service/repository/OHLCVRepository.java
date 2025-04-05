package com.bootcamp.bc_xfin_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bootcamp.bc_xfin_service.entity.OHLCVEntity;

@Repository
public interface OHLCVRepository extends JpaRepository<OHLCVEntity, Long> {
    
}
