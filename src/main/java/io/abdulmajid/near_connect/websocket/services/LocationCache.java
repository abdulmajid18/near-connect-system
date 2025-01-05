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

    private final LocationService locationService;


    public LocationCache(CacheManager cacheManager, LocationService locationService) {
        this.cacheManager = cacheManager;
        this.locationService = locationService;
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
            return valueWrapper != null ? (LocationDTO) valueWrapper.get() : getLocationWithFallback(userId);
        }
        return null;
    }

    private LocationDTO getLocationWithFallback(String userId) {
        LocationDTO cachedLocation = getLocationHistoryFromCache(userId);
        if (cachedLocation == null) {
            // Fallback to database
            LocationDTO dbLocation = locationService.getLatestLocation(userId);
            if (dbLocation != null) {
                cacheLocation(userId, dbLocation);
                return dbLocation;
            }
        }
        return cachedLocation;
    }
}
