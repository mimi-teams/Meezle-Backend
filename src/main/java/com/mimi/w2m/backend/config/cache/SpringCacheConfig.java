package com.mimi.w2m.backend.config.cache;

import com.mimi.w2m.backend.dto.landing.LandingResponseDto;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.jsr107.Eh107Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.Caching;
import java.time.Duration;
import java.util.Objects;

/**
 * SpringCacheConfig
 *
 * @author teddy
 * @version 1.0.0
 * @since 2023/01/18
 **/

@Configuration
@EnableCaching(mode = AdviceMode.PROXY)
public class SpringCacheConfig {
    private final Logger logger = LoggerFactory.getLogger(SpringCacheConfig.class.getName());

    @Bean
    public CacheManager ehcacheManager() {
        final var provider = Caching.getCachingProvider();
        final var manager = provider.getCacheManager();
        final var configuration = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(SimpleKey.class, LandingResponseDto.class,
                        ResourcePoolsBuilder.newResourcePoolsBuilder().offheap(1, MemoryUnit.MB))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMinutes(1)));
        final var eh107Configuration = Eh107Configuration.fromEhcacheCacheConfiguration(configuration);
        final var cacheName = "landingInfo";
        // Test에서 Context를 생성할 때, 여러 Cache 가 생성되는 경우가 발생한다. 이를 방지하기 위해 Singleton 형식으로 생성하기!
//        synchronized (this) {
            if (Objects.isNull(manager.getCache(cacheName))) {
                manager.createCache(cacheName, eh107Configuration);
            }
//        }
        return manager;
    }
}