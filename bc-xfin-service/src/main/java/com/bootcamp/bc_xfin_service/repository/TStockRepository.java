package com.bootcamp.bc_xfin_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bootcamp.bc_xfin_service.entity.TStockEntity;

@Repository
public interface TStockRepository extends JpaRepository<TStockEntity, Long> {
    Optional<TStockEntity> findBySymbol(String symbol);
}
