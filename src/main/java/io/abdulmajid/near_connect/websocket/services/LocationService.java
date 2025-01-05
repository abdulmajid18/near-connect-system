package io.abdulmajid.near_connect.websocket.services;

import io.abdulmajid.near_connect.websocket.dtos.LocationDTO;
import io.abdulmajid.near_connect.websocket.entities.LocationHistory;
import io.abdulmajid.near_connect.websocket.repositories.LocationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LocationService {
    private final LocationRepository locationRepository;
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public LocationDTO saveLocationHistory(LocationDTO dto) {
        LocationHistory locationHistory = LocationHistory.create(dto.getUserId(), dto.getLatitude(), dto.getLongitude());

        LocationHistory saved = locationRepository.save(locationHistory);
        log.info("Location History saved  " + saved);
        return new LocationDTO(saved.getUserId(), saved.getLatitude(), saved.getLongitude(), saved.getTimestamp());
    }

    public List<LocationDTO> getLocationHistoryForUser(String userId) {
        return locationRepository.findByUserId(userId).stream()
                .map(lh -> new LocationDTO(lh.getUserId(), lh.getLatitude(), lh.getLongitude(), lh.getTimestamp()))
                .toList();
    }

    public List<LocationDTO> getLatestLocationHistory(String userId) {
        return locationRepository.findByUserIdOrderByTimestampDesc(userId).stream()
                .map(lh -> new LocationDTO(lh.getUserId(), lh.getLatitude(), lh.getLongitude(), lh.getTimestamp()))
                .toList();
    }

    public LocationDTO getLatestLocation(String userId) {
        Optional<LocationHistory> optionalLocationHistory = locationRepository.findTopByUserIdOrderByTimestampDesc(userId);

        if (optionalLocationHistory.isPresent()) {
            LocationHistory locationHistory = optionalLocationHistory.get();
            return new LocationDTO(locationHistory.getUserId(), locationHistory.getLatitude(),
                    locationHistory.getLongitude(), locationHistory.getTimestamp());
        } else {
            return null;
        }
    }

}
