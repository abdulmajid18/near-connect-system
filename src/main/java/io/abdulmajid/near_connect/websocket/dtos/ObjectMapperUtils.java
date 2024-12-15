package io.abdulmajid.near_connect.websocket.dtos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Slf4j
@Component
public class ObjectMapperUtils {
    private final ObjectMapper objectMapper;

    @Autowired
    public ObjectMapperUtils(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Optional<LocationDTO> deserializeLocation(String payload) throws JsonProcessingException {
        try {
            return Optional.ofNullable(objectMapper.readValue(payload, LocationDTO.class));
        } catch (JsonProcessingException e) {
            log.error("Error deserializing payload: {}", payload, e);
            return Optional.empty();
        }
    }

    public Optional<String> serializeLocation(LocationDTO locationDTO) {
        try {
            return  Optional.ofNullable(objectMapper.writeValueAsString(locationDTO));
        } catch (JsonProcessingException e) {
            log.error("Error serializing LocationDTO: {}", locationDTO, e);
            return Optional.empty();
        }
    }

}
