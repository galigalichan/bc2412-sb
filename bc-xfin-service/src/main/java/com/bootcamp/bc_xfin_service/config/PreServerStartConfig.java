package com.bootcamp.bc_xfin_service.config;

import java.time.Duration;
import java.util.List;

import org.hibernate.StaleObjectStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.bootcamp.bc_xfin_service.entity.TStockEntity;
import com.bootcamp.bc_xfin_service.repository.TStockRepository;

import jakarta.transaction.Transactional;

@Component
@Transactional
public class PreServerStartConfig implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(PreServerStartConfig.class);

    @Autowired
    private RedisManager redisHelper;

    @Autowired
    private StocksProperties stocksProperties;
    
    @Autowired
    private TStockRepository tStockRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Define the Redis key for the stock list
        String redisKey = "STOCK-LIST";
    
        // Clear stock list cache on startup
        redisHelper.delete(redisKey);
        log.info("Cleared Redis entry: {}", redisKey);
    
        List<String> stocks = stocksProperties.getSymbols();
    
        // Store the stock list in Redis
        redisHelper.set(redisKey, stocks, Duration.ofHours(12));
        log.info("Stored stock list in Redis under key: {}", redisKey);
    
        // Delete all system date and 5-min Redis entries
        for (String stock : stocks) {
            redisHelper.delete("SYSDATE-" + stock);
            redisHelper.delete("5MIN-" + stock);
            log.info("Cleared Redis system date and 5-min data for: {}", stock);
    
            // Ensure stock exists in DB
            if (tStockRepository.findBySymbol(stock).isEmpty()) {
                TStockEntity tStockEntity = new TStockEntity();
                tStockEntity.setSymbol(stock);
                try {
                    tStockRepository.save(tStockEntity);
                } catch (StaleObjectStateException e) {
                    log.error("Failed to save stock {}: {}", stock, e.getMessage());
                }
            }
        }
    }
}
