package com.vamo.common.dto;

import java.util.UUID;
import lombok.Builder;

@Builder
public record SubscriptionRequestDTO(
		UUID receiverID,
		String firstName,
		String lastName,
		String corridorTitle,
		String fees,
		String phoneNumber
) {

}
