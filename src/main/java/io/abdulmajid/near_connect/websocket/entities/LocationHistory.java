package io.abdulmajid.near_connect.websocket.entities;

import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("location_history")
public class LocationHistory {
    @PrimaryKey
    @CassandraType(type=CassandraType.Name.UUID)
    private UUID id;

    @Column
    private double latitude;

    @Column
    private double longitude;

    @Column
    private LocalDateTime timestamp;

    @Column
    private String userId;

    public LocationHistory() {
        this.id = UUID.randomUUID();
    }

    public LocationHistory(String userId, LocalDateTime timestamp, double latitude, double longitude) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "LocationHistory{" +
                "userId='" + userId + '\'' +
                ", timestamp=" + timestamp +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public static LocationHistory create(String userId, double latitude, double longitude) {
        return new LocationHistory(userId, LocalDateTime.now(), latitude, longitude);
    }
}
