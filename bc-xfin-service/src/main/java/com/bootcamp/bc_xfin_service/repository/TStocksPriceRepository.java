package com.bootcamp.bc_xfin_service.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bootcamp.bc_xfin_service.entity.TStocksPriceEntity;

@Repository
public interface TStocksPriceRepository extends JpaRepository<TStocksPriceEntity, Long> {
    Optional<TStocksPriceEntity> findBySymbol(String symbol);
    
    Optional<TStocksPriceEntity> findTopBySymbolOrderByRegularMarketTimeDesc(String symbol);

    @Query(value = "SELECT * FROM tstocks_price WHERE symbol = :symbol AND DATE(TO_TIMESTAMP(regularmarkettime)) = :marketDate ORDER BY regularmarkettime DESC LIMIT 1", nativeQuery = true)
    Optional<TStocksPriceEntity> findTopBySymbolAndMarketDateOrderByRegularMarketTimeDesc(@Param("symbol") String symbol, @Param("marketDate") LocalDate marketDate);
    
    @Query(value = "SELECT * FROM tstocks_price WHERE symbol = :symbol AND DATE(TO_TIMESTAMP(regularmarkettime)) = :marketDate ORDER BY regularmarkettime DESC", nativeQuery = true)
    List<TStocksPriceEntity> findBySymbolAndMarketDateDesc(@Param("symbol") String symbol, @Param("marketDate") LocalDate marketDate);

    // @Query(value = "SELECT * FROM tstocks_price WHERE symbol = :symbol AND DATE(TO_TIMESTAMP(regularmarkettime)) = :marketDate ORDER BY regularmarkettime ASC", nativeQuery = true)
    // List<TStocksPriceEntity> findBySymbolAndMarketDate(@Param("symbol") String symbol, @Param("marketDate") LocalDate marketDate);

}
