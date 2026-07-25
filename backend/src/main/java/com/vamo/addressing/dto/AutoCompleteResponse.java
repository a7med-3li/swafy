package com.vamo.addressing.dto;

import com.vamo.addressing.entity.Address;

import java.util.List;

public record AutoCompleteResponse(String source, List<Address> addresseList) {}
