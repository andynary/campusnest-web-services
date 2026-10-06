package edu.upc.campusnest.mapper;

import edu.upc.campusnest.dto.response.PropertyResponse;
import edu.upc.campusnest.model.Property;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = RoomMapper.class)
public interface PropertyMapper {
    @Mapping(source = "district.name", target = "district")
    PropertyResponse toResponse(Property property);
}
