package com.vamo.common.events;

import com.vamo.common.dto.DriverRegisterRequest;
import com.vamo.user.entity.User;

public record DriverRegisteredEvent(User user, DriverRegisterRequest registerDriverRequest) {

}
