package com.swafy.ride.dto;

import java.util.List;

public record HereRouteResponse(List<Route> routes) {

    public record Route(List<Section> sections) {}

    public record Section(Summary summary) {}

    public record Summary(int length, int duration) {}
}