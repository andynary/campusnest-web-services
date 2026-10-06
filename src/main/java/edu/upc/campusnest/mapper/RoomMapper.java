package edu.upc.campusnest.mapper;

import edu.upc.campusnest.dto.response.RoomResponse;
import edu.upc.campusnest.model.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    @Mapping(source = "property.id", target = "propertyId")
    RoomResponse toResponse(Room room);
}
