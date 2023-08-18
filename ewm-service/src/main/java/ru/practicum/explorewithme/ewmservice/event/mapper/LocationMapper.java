package ru.practicum.explorewithme.ewmservice.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.ewmservice.event.dto.LocationDTO;
import ru.practicum.explorewithme.ewmservice.event.model.Location;

@UtilityClass
public class LocationMapper {
    public static Location toLocation(LocationDTO locationDTO) {
        return Location.builder()
                .lat(locationDTO.getLat())
                .lon(locationDTO.getLon())
                .build();
    }

    public static LocationDTO toLocationDTO(Location location) {
        return LocationDTO.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }
}
