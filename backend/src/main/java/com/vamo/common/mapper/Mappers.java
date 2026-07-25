package com.vamo.common.mapper;

import com.vamo.user.dto.UpdateUserRequest;
import com.vamo.user.dto.UserInfo;
import com.vamo.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface Mappers {
    // Placeholder for mapping utilities (e.g., MapStruct wrappers)
	
	@Mapping(target = "displayName", expression = "java(formatDisplayName(user.getFirstName(), user.getLastName()))")
	UserInfo userToUserInfo(User user);
	
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
