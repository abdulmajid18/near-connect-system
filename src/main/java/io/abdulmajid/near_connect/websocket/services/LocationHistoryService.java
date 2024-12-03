package io.abdulmajid.near_connect.websocket.services;

import io.abdulmajid.near_connect.websocket.dtos.LocationHistoryDTO;
import io.abdulmajid.near_connect.websocket.entities.LocationHistory;
import io.abdulmajid.near_connect.websocket.repositories.LocationHistoryRepository;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationHistoryService {
    private final LocationHistoryRepository locationHistoryRepository;
    public LocationHistoryService(LocationHistoryRepository locationHistoryRepository) {
        this.locationHistoryRepository = locationHistoryRepository;
    }

    @CachePut(value = "locationHistoryCache", key = "#dto.userId")
    public LocationHistoryDTO saveLocationHistory(LocationHistoryDTO dto) {
        LocationHistory locationHistory = LocationHistory.create(dto.getUserId(), dto.getLatitude(), dto.getLongitude());

        LocationHistory saved = locationHistoryRepository.save(locationHistory);
        return new LocationHistoryDTO(saved.getUserId(), saved.getLatitude(), saved.getLongitude(), saved.getTimestamp());
    }

    public List<LocationHistoryDTO> getLocationHistoryForUser(String userId) {
        return locationHistoryRepository.findByUserId(userId).stream()
                .map(lh -> new LocationHistoryDTO(lh.getUserId(), lh.getLatitude(), lh.getLongitude(), lh.getTimestamp()))
                .toList();
    }

    public List<LocationHistoryDTO> getLatestLocationHistory(String userId) {
        return locationHistoryRepository.findByUserIdOrderByTimestampDesc(userId).stream()
                .map(lh -> new LocationHistoryDTO(lh.getUserId(), lh.getLatitude(), lh.getLongitude(), lh.getTimestamp()))
                .toList();
    }
}
