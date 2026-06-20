package com.swafy.common.events;

import com.swafy.common.dto.DriverRegisterRequest;
import com.swafy.user.entity.User;

public record DriverRegisteredEvent(User user, DriverRegisterRequest registerDriverRequest) {

}
