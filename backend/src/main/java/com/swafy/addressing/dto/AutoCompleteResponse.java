package com.swafy.addressing.dto;

import com.swafy.addressing.entity.Address;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AutoCompleteResponse {
    private String source;
    private List<Address> addresseList = new ArrayList<>();
}
