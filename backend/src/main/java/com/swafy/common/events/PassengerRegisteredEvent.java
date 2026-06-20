package com.swafy.common.events;

import com.swafy.common.dto.PassengerRegisterRequest;
import com.swafy.user.entity.User;

public record PassengerRegisteredEvent(User user, PassengerRegisterRequest registerPassengerRequest) {

}
