package com.swafy.common.mapper;

import com.swafy.user.dto.UpdateUserRequest;
import com.swafy.user.dto.UserInfo;
import com.swafy.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface Mappers {
    // Placeholder for mapping utilities (e.g., MapStruct wrappers)
	
	@Mapping(target = "displayName", expression = "java(formatDisplayName(user.getFirstName(), user.getLastName()))")
	UserInfo userToUserInfo(User user);
	
	@Mapping(target = "id", ignore = true)
	void updateUserFromRequest(UpdateUserRequest request, @MappingTarget User entity);
	
	default String formatDisplayName(String firstName, String lastName) {
		if (firstName == null && lastName == null) {
			return null;
		}
		
		String display = firstName != null ? firstName : "";
	
		if (lastName != null && !lastName.trim().isEmpty()) {
			display += " " + lastName.trim().charAt(0) + ".";
		}
		
		return display.trim();
	}
	
}
