package com.swafy.ride.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swafy.addressing.entity.Address;
import com.swafy.addressing.service.interfaces.UpdatingAddressCache;
import com.swafy.common.entity.GeoPoint;
import com.swafy.ride.domain.RouteInfo;
import com.swafy.ride.dto.HereDiscoverResponse;
import com.swafy.ride.dto.HereRouteResponse;
import com.swafy.ride.service.interfaces.RoutingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Qualifier("HereTechnologies")
@Primary
@Slf4j
public class HERERoutingServiceImpl implements RoutingService {

    // TODO find a better way for the api integration and consume the response

    private final UpdatingAddressCache updatingAddressCache;
    @Qualifier("routingClient")
    private final WebClient routeClient;

    @Qualifier("searchClient")
    private final WebClient searchClient;


    @Value("${here.api.key}")
    private String HERE_API_KEY;
    @Override
    public RouteInfo calculateRouteInfo(GeoPoint from, GeoPoint to) {
        String origin = from.getLatitude() + "," + from.getLongitude();
        String destination = to.getLatitude() + "," + to.getLongitude();

        HereRouteResponse response = routeClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("origin", origin)
                        .queryParam("destination", destination)
                        .queryParam("transportMode", "car")
                        .queryParam("return", "summary")
                        .queryParam("apiKey", HERE_API_KEY)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .map(body -> new RuntimeException("HERE API error: " + body))
                )
                .bodyToMono(HereRouteResponse.class)
                .block(); // block because we are in MVC app

        return mapToRouteInfo(response)
                .orElse(RouteInfo.empty());
    }

    @Override
    public Address search(String location) {
        String at = "29.0667,31.0833";
        HereDiscoverResponse response = searchClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("q", location)
                        .queryParam("at", at)
                        .queryParam("limit", 1)
                        .queryParam("apiKey", HERE_API_KEY)
                        .build())
                .retrieve()
                .bodyToMono(HereDiscoverResponse.class)
                .block();

        HereDiscoverResponse.Item item = response.getItems().getFirst();
        Address address = Address.builder()
                .address(item.getTitle())
                .latitude(item.getPosition().getLat())
                .longitude(item.getPosition().getLng())
                .build();

        log.info(response.toString());

        try {
            updatingAddressCache.storeAddress(address);
        } catch (Exception e) {
            log.info("the address is already there");
        }
        return address;
    }

    private Optional<RouteInfo> mapToRouteInfo(HereRouteResponse response) {

        if (response == null
                || response.getRoutes() == null
                || response.getRoutes().isEmpty()) {
            return Optional.of(RouteInfo.empty());
        }

        HereRouteResponse.Summary summary =
                response.getRoutes()
                        .getFirst()
                        .getSections()
                        .getFirst()
                        .getSummary();

        return Optional.of(
                new RouteInfo(
                        summary.getLength(),
                        Duration.ofSeconds(summary.getDuration())
                )
        );
    }

}
