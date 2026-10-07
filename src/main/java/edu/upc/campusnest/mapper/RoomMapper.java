package edu.upc.campusnest.mapper;

import edu.upc.campusnest.dto.response.RoomResponse;
import edu.upc.campusnest.model.IncludedService;
import edu.upc.campusnest.model.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    @Mapping(source = "property.id", target = "propertyId")
    RoomResponse toResponse(Room room);

    /** Convierte cada servicio incluido en su nombre (US 10). */
    default String serviceName(IncludedService service) {
        return service.getName();
    }
}
