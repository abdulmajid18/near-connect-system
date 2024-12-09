package io.abdulmajid.near_connect.websocket.dtos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectMapperUtils {
    private static final Logger logger = LoggerFactory.getLogger(ObjectMapperUtils.class);

    public static LocationHistoryDTO deserializeLocation(String payload) throws JsonProcessingException {
        // Initialize ObjectMapper and register the JavaTimeModule to handle LocalDateTime
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

        try {
            return objectMapper.readValue(payload, LocationHistoryDTO.class);
        } catch (JsonProcessingException e) {
            logger.error("Error deserializing payload: {}", payload, e);
            return null;
        }
    }
}
