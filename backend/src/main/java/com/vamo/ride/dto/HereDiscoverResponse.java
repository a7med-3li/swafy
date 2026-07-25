package com.vamo.ride.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record HereDiscoverResponse(@JsonProperty("items") List<Item> items) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Item(
            @JsonProperty("title") String title,
            @JsonProperty("position") Position position
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Position(
            @JsonProperty("lat") String lat,
            @JsonProperty("lng") String lng
    ) {}
}
