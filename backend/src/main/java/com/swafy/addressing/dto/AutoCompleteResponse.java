package com.swafy.addressing.dto;

import com.swafy.addressing.entity.Address;

import java.util.List;

public record AutoCompleteResponse(String source, List<Address> addresseList) {}