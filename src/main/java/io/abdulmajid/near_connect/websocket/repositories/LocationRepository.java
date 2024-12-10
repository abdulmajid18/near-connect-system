package io.abdulmajid.near_connect.websocket.repositories;

import io.abdulmajid.near_connect.websocket.entities.LocationHistory;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.List;
import java.util.UUID;

public interface LocationRepository extends CassandraRepository<LocationHistory, UUID> {
    List<LocationHistory> findByUserId(String userId);
    List<LocationHistory> findByUserIdOrderByTimestampDesc(String userId);

}
