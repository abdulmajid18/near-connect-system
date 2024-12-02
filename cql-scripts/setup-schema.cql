-- setup-schema.cql
-- Create the location_history table
CREATE TABLE IF NOT EXISTS near_connect.location_history (
    id UUID PRIMARY KEY,
    latitude DOUBLE,
    longitude DOUBLE,
    timestamp TIMESTAMP,
    userId TEXT
);
