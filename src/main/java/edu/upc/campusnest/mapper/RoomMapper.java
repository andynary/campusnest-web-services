package edu.upc.campusnest.mapper;

import edu.upc.campusnest.dto.response.RoomResponse;
import edu.upc.campusnest.model.IncludedService;
import edu.upc.campusnest.model.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    @Mapping(source = "property.id", target = "propertyId")
    @Mapping(source = "property.district.name", target = "district")
    @Mapping(target = "compatibilityScore", ignore = true)   // lo calcula SearchService
    RoomResponse toResponse(Room room);

    /** Room.services (entidades) -> RoomResponse.services (nombres ordenados). */
    default List<String> servicesToNames(Set<IncludedService> services) {
        if (services == null) return List.of();
        return services.stream().map(IncludedService::getName).sorted(Comparator.naturalOrder()).toList();
    }
}
