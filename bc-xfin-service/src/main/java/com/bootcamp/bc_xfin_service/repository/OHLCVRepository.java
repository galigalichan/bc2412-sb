package com.bootcamp.bc_xfin_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bootcamp.bc_xfin_service.entity.OHLCVEntity;

@Repository
public interface OHLCVRepository extends JpaRepository<OHLCVEntity, Long> {
    @Query("""
        FROM OHLCVEntity p
        WHERE LOWER(p.symbol) = LOWER(:symbol)
        AND p.timestamp BETWEEN :startEpoch AND :endEpoch
        ORDER BY p.timestamp
    """)
    List<OHLCVEntity> findBySymbolAndTimestampRangeCustom(
        @Param("symbol") String symbol,
        @Param("startEpoch") Long startEpoch,
        @Param("endEpoch") Long endEpoch
    );

    @Modifying
    @Query("UPDATE OHLCVEntity o SET o.timestamp = :newTimestamp WHERE o.id = :id")
    int updateTimestampById(@Param("id") Long id, @Param("newTimestamp") Long newTimestamp);

    @Query("""
        FROM OHLCVEntity o
        WHERE LOWER(o.symbol) = LOWER(:symbol)
        AND o.timestamp IN :timestamps
        ORDER BY o.timestamp
    """)
    List<OHLCVEntity> getEntitiesBySymbolAndTimestamps(
        @Param("symbol") String symbol,
        @Param("timestamps") List<Long> timestamps
    );

    @Query(value = "SELECT * FROM tstocks_price_ohlcv WHERE symbol = :symbol AND timestamp < :earliestTimestamp ORDER BY timestamp DESC LIMIT :limit", nativeQuery = true)
    List<OHLCVEntity> findPreviousEntries(@Param("symbol") String symbol, @Param("earliestTimestamp") long earliestTimestamp, @Param("limit") int limit);

    // @Modifying
    // @Query("DELETE FROM OHLCVEntity o WHERE o.symbol = :symbol AND o.timestamp BETWEEN :start AND :end")
    // void deleteBySymbolAndDate(@Param("symbol") String symbol, @Param("start") long startEpoch, @Param("end") long endEpoch);
}
