package com.swafy.ride.dto;

import lombok.Data;
import java.util.List;

@Data
public class HereRouteResponse {

    private List<Route> routes;

    @Data
    public static class Route {
        private List<Section> sections;
    }

    @Data
    public static class Section {
        private Summary summary;
    }

    @Data
    public static class Summary {
        private int length;     // meters
        private int duration;   // seconds
    }
}
