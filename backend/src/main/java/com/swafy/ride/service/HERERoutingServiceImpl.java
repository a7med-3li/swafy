package com.swafy.ride.service;

import com.swafy.common.entity.GeoPoint;
import com.swafy.ride.domain.RouteInfo;
import com.swafy.ride.service.interfaces.RoutingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Qualifier("HereTechnologies")
@Primary
public class HERERoutingServiceImpl implements RoutingService {

    private final String BASE_URL ="https://router.hereapi.com/v8/routes";
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${here.api.key}")
    private String HERE_API_KEY;
    @Override
    public RouteInfo calculateRouteInfo(GeoPoint from, GeoPoint to) {
        String origin = STR."\{from.getLatitude()},\{from.getLongitude()}";
        String destination = STR."\{to.getLatitude()},\{to.getLongitude()}";
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("origin",origin)
                .queryParam("destination", destination)
                .queryParam("transportMode", "car")
                .queryParam("return", "summary,polyline")
                .queryParam("apiKey", HERE_API_KEY)
                .toUriString();

        String response = restTemplate.getForObject(url, String.class);
        assert response != null;
        System.out.printf(response);
        return null;
    }
}
