package io.abdulmajid.near_connect.websocket.services;

import io.abdulmajid.near_connect.websocket.dtos.LocationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LocationCache {

    private final CacheManager cacheManager;

    public LocationCache(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @SuppressWarnings("ConstantConditions")
    public void cacheLocation(String userId, LocationDTO location) {
        Cache cache = cacheManager.getCache(CacheName.LOCATION_CACHE.getCacheName());
        if (cache != null) {
            cache.put(userId, location);
            log.info("Location cached for userId: " + userId);
        } else {
            log.warn("Cache not found");
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void evictCache(String userId) {
        Cache cache = cacheManager.getCache(CacheName.LOCATION_CACHE.getCacheName());
        if (cache != null) {
            log.info("Evicting cache for userId: {}", userId);
            cache.evict(userId);
        } else {
            log.warn("Cache not found for eviction: location");
        }
    }

    @SuppressWarnings("ConstantConditions")
    public LocationDTO getLocationHistoryFromCache(String userId) {
        Cache cache = cacheManager.getCache(CacheName.LOCATION_CACHE.getCacheName());  // Get the cache by name
        if (cache != null) {
            Cache.ValueWrapper valueWrapper = cache.get(userId);  // Retrieve the object by key
            return valueWrapper != null ? (LocationDTO) valueWrapper.get() : null;
        }
        return null;
    }
}
