package com.vamo.ride.service;

import com.vamo.addressing.entity.Address;
import com.vamo.addressing.service.interfaces.UpdatingAddressCache;
import com.vamo.common.entity.Location;
import com.vamo.ride.domain.RouteInfo;
import com.vamo.ride.dto.HereDiscoverResponse;
import com.vamo.ride.dto.HereRouteResponse;
import com.vamo.ride.service.interfaces.RoutingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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
    public RouteInfo calculateRouteInfo(Location from, Location to) {
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

        HereDiscoverResponse.Item item = response.items().getFirst();
        Address address = Address.builder()
                .address(item.title())
                .latitude(item.position().lat())
                .longitude(item.position().lng())
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
                || response.routes() == null
                || response.routes().isEmpty()) {
            return Optional.of(RouteInfo.empty());
        }

        HereRouteResponse.Summary summary =
                response.routes()
                        .getFirst()
                        .sections()
                        .getFirst()
                        .summary();

        return Optional.of(new RouteInfo(summary.length(), Duration.ofSeconds(summary.duration())));
    }

}
