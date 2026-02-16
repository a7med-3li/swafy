package com.swafy.ride.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HereDiscoverResponse {
    @JsonProperty("items")
    private List<Item> items;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        @JsonProperty("title")
        private String title;
        @JsonProperty("position")
        private Position position;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Position {
        @JsonProperty("lat")
        private String lat;
        @JsonProperty("lng")
        private String lng;
    }
}
