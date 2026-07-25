package com.vamo.common.events;

import com.vamo.common.dto.PassengerRegisterRequest;
import com.vamo.user.entity.User;

public record PassengerRegisteredEvent(User user, PassengerRegisterRequest registerPassengerRequest) {

}
