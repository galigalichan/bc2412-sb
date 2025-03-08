package com.bootcamp.sb.demo_bc_mtr_station.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bootcamp.sb.demo_bc_mtr_station.entity.StationEntity;
import com.bootcamp.sb.demo_bc_mtr_station.entity.code.StationCode;

@Repository
public interface StationRepository extends JpaRepository<StationEntity, Long> {

    List<StationEntity> findByStationCode(StationCode stationCode);
    
}
