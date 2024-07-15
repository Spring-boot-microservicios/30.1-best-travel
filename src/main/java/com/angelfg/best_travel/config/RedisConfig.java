package com.angelfg.best_travel.config;

import com.angelfg.best_travel.util.constants.CacheConstants;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Configuration
@EnableCaching // Autoconfiguracion
@EnableScheduling // Permite hacer tareas calendarizadas
@Slf4j
public class RedisConfig {

    @Value(value = "${cache.redis.address}")
    private String serverAddress;

    @Value(value = "${cache.redis.password}")
    private String serverPassword;

    // Configuracion principal de redis
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();

        config.useSingleServer()
                .setAddress(serverAddress)
                .setPassword(serverPassword);

        return Redisson.create(config);
    }

    // Cache de spring
    @Bean
    @Autowired // necesitamos inyectar redissonClient requerimos el Autowired
    public CacheManager cacheManager(RedissonClient redissonClient) {
        var configs = Map.of(
            CacheConstants.FLY_CACHE_NAME, new CacheConfig(),
            CacheConstants.HOTEL_CACHE_NAME, new CacheConfig()
        );

        return new RedissonSpringCacheManager(redissonClient, configs);
    }

    // Limpia los cache
    @CacheEvict(cacheNames = {
        CacheConstants.FLY_CACHE_NAME,
        CacheConstants.HOTEL_CACHE_NAME
    }, allEntries = true)
    @Scheduled(cron = CacheConstants.SCHEDULED_RESET_CACHE) // Permite que este metodo se ejecute de forma calendarizada
    @Async // Lo ejecute en un hilo aparte
    public void deleteCache() {
        log.info("Clean cache: " + LocalDateTime.now());
    }

}
