package com.shalom.shipping_status.controller;

import com.shalom.shipping_status.model.request.SetEmailShipShalomRequest;
import com.shalom.shipping_status.model.request.ShipShalomRequest;
import com.shalom.shipping_status.model.response.SearchShalomResponse;
import com.shalom.shipping_status.service.shalom.IShalomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/api/ship-shalom")
public class ShipShalomRouterController {
    private final IShalomService service;

    @PostMapping("/states")
    public ResponseEntity<Mono<SearchShalomResponse>> getPackage(@RequestBody ShipShalomRequest request) {
        return ok(this.service.getPackage(request));
    }

    @PutMapping("/set-email")
    public ResponseEntity<Mono<Map<String, String>>> setEmail(@RequestBody SetEmailShipShalomRequest request) {
        return ok(this.service.setEmail(request));
    }
}
