package edu.upc.campusnest.mapper;

import edu.upc.campusnest.dto.response.UserResponse;
import edu.upc.campusnest.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User user);
}
